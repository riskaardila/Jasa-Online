package com.example.jasaonline.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.jasaonline.R
import com.example.jasaonline.activities.EditProfileActivity
import com.example.jasaonline.activities.LoginActivity
import com.example.jasaonline.helpers.SessionHandler
import com.example.jasaonline.models.DefaultResponse
import com.example.jasaonline.models.User
import com.example.jasaonline.services.ServiceBuilder
import com.example.jasaonline.services.UserService
import kotlinx.android.synthetic.main.fragment_profile.view.*
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionHandler(requireContext())
        val user: User? = session.getUser()
        val titikDua = ": "

        if(user != null) {
            view.tvNama.text = titikDua + user.nama
            view.tvTanggalLahir.text = titikDua + user.tanggalLahir
            view.tvJenisKelamin.text = titikDua + user.jenisKelamin
            view.tvNomorHP.text = titikDua + user.nomorHP
            view.tvAlamat.text = titikDua + user.alamat
            view.tvEmail.text = titikDua + user.email
            view.tvWaktuSesi.text = titikDua + session.getExpireTime()
        }
        view.btnEditProfil.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        }

        view.btnHapusUser.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Hapus Akun")
            builder.setMessage("Apakah anda yakin menghapus akun?")
            builder.setIcon(R.drawable.ic_delete_forever_black_24dp)
            builder.setPositiveButton("Yes"){dialog, _ ->

                val userService: UserService = ServiceBuilder.buildService(UserService::class.java)
                val requestCall: Call<DefaultResponse> = userService.deleteUser(user?.id!!)

                requestCall.enqueue(object:
                    retrofit2.Callback<DefaultResponse>{
                    override fun onFailure(call: Call<DefaultResponse>, t:
                    Throwable) {
                        Toast.makeText(context, "Error terjadi ketika sedang menghapus user: " + t.toString(), Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if(!response.body()?.error!!) {
                            val defaultResponse: DefaultResponse = response.body()!!
                            defaultResponse.let {
                                session.removeUser()
                                Toast.makeText(context,
                                    defaultResponse.message, Toast.LENGTH_LONG).show()
                                val intent = Intent(context, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        }else{
                            Toast.makeText(context, "Gagal menghapus user:" + response.body()?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
                dialog.dismiss()
            }

            builder.setNegativeButton("No"){dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }
}

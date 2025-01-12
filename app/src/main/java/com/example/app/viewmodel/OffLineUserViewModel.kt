package com.example.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.app.data.User
import com.example.app.data.UserDataBase
import com.example.app.data.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

class OffLineUserViewModel (private val userRepository: UserRepository): ViewModel() {

    suspend fun guardarsuario(user: User){
        userRepository.insert(user)
    }

    suspend fun validarUsuario(user : User): Boolean{

        val recoverUser = userRepository.getUsers(user.email)
            if(recoverUser == null){ //si el correo no esta registrado se debe guardar el usuario
                guardarsuario(user)
                return true
            }
            else{
                return false
            }
        }

    data class RespuestaSesion(val pasar : Boolean, val mensaje : String = "")

    suspend fun leerUsuario(email: String, password: String): RespuestaSesion{
        val recoverUser = userRepository.getUsers(email)
        if(recoverUser == null){ //no existe
            return RespuestaSesion(false,"Usuario no existe")
        }
        else{
            if(recoverUser.password.equals(password)){ //deja pasar al usuario
                return RespuestaSesion(true)
            }else{
                return RespuestaSesion(false,"Contraseña incorrecta") // tu contrasena es incorrecta
            }
        }
    }
}
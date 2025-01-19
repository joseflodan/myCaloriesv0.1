package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.data.user.User
import com.example.app.data.user.UserRepository

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
    suspend fun updateCalories (email: String, calories: Double){
        val user = userRepository.getUsers(email)
        user.calorias = calories
        userRepository.update(user)
    }
}
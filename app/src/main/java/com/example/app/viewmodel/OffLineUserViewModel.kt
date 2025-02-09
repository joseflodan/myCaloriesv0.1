package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.MyApp.Companion.EJERCICIO
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

    suspend fun updateImc (email: String, imc: Double){
        val user = userRepository.getUsers(email)
        user.imc = imc
        userRepository.update(user)
    }

    fun getCalorias(email: String): Double {
        val user = userRepository.getUsers(email)
        return user.calorias
    }

    suspend fun guardarDatos(
        email: String,
        sexo: Boolean,
        altura: String,
        peso: String,
        edad: String,
        imc: Float,
        ejercicioSeleccionado: String
    ) {
        val user = userRepository.getUsers(email)
        user.sexo = sexo
        user.altura = altura.toInt()
        user.peso = peso.toInt()
        user.edad = edad.toInt()
        user.imc = imc.toDouble()
        user.frecuencia = ejercicioSeleccionado
        user.tmb = calcularTMB(sexo,altura.toDouble(),peso.toDouble(),edad.toDouble(), ejercicioSeleccionado.toString())

        userRepository.update(user)
    }

    private fun calcularTMB(sexo: Boolean, altura: Double, peso: Double, edad:  Double, ejercicioSeleccionado: String): Double {
        var tmb = 10 * peso + 6.25 * altura - 5 * edad

        tmb = if (sexo){//hombre
            tmb + 5
        }else{//mujer
            tmb - 161
        }

        tmb = when(ejercicioSeleccionado){
            EJERCICIO[0] -> tmb * 1.2
            EJERCICIO[1] -> tmb * 1.375
            EJERCICIO[2] -> tmb * 1.55
            EJERCICIO[3] -> tmb * 1.725
            EJERCICIO[4] -> tmb * 1.9
            else -> tmb
        }
        return tmb
    }
}
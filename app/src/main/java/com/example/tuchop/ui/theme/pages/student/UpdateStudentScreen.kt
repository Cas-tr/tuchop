package com.example.tuchop.ui.theme.pages.student

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tuchop.data.StudentRepository
import com.example.tuchop.models.Student
import com.example.tuchop.navigation.ROUTE_HOME
import com.example.tuchop.navigation.ROUTE_VIEW_STUDENTS_SCREEN
import com.example.tuchop.ui.theme.TuchopTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateStudentScreen(navController: NavHostController,id:String) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { navController.navigate(ROUTE_HOME) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Red
                )) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "")
            }
        }
        var context = LocalContext.current
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }
        var levelOfEducation by remember { mutableStateOf("") }

        var currentDataRef = FirebaseDatabase.getInstance().getReference()
            .child("Student/$id")
        currentDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var student = snapshot.getValue(Student::class.java)
                name = student!!.name
                email = student!!.email
                phoneNumber = student!!.phoneNumber
                levelOfEducation = student!!.levelOfEducation
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        Text(
            text = "Update student",
            fontSize = 30.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.Red,
            modifier = Modifier.padding(20.dp),
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )

        var studentName by remember { mutableStateOf(TextFieldValue(name)) }
        var studentEmail by remember { mutableStateOf(TextFieldValue(email)) }
        var studentPhoneNumber by remember { mutableStateOf(TextFieldValue(phoneNumber)) }
        var studentLevelOfEducation by remember { mutableStateOf(TextFieldValue(levelOfEducation)) }

        OutlinedTextField(
            value = studentName,
            onValueChange = { studentName = it },
            label = { Text(text = "Student name *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = studentEmail,
            onValueChange = { studentEmail = it },
            label = { Text(text = "Student Email *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = studentPhoneNumber,
            onValueChange = { studentPhoneNumber = it },
            label = { Text(text = "Student PhoneNumber* start with 7..0r 1..") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = studentLevelOfEducation,
            onValueChange = { studentLevelOfEducation = it },
            label = { Text(text = "Student Level Of Education *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )


        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            //-----------WRITE THE UPDATE LOGIC HERE---------------//
            var studentRepository = StudentRepository(navController, context)
            studentRepository.updateStudent(
                studentName.text.trim(), studentEmail.text.trim(),
                studentPhoneNumber.text.trim(), studentLevelOfEducation.text.trim(), id
            )
            navController.navigate(ROUTE_VIEW_STUDENTS_SCREEN)
        },colors = ButtonDefaults.buttonColors(containerColor = Color.Red,contentColor = Color.White)) {
            Text(text = "Update")
        }
    }
}

@Preview
@Composable
fun UpdateStudentScreenPreview(){
    TuchopTheme {
        UpdateStudentScreen(rememberNavController(),id = "")
    }
}
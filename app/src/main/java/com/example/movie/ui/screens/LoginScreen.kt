package com.example.movie.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.movie.database.model.UserEntity
import com.example.movie.viewmodel.LoginViewModel
import com.example.movie.viewmodel.events.FilterType
import com.example.movie.viewmodel.events.LoginOrRegister

@Composable
fun LoginScreen(
    nav : NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.observeAsState()

    if (state?.isVerifying == true) {
        Text(text = "Loading...")
    } else {
        if (state!!.isSuccess) {
            LaunchedEffect(Unit) {
                nav.navigate("movieList/${state!!.lastUserName}")
            }
        }

        Column {
            UserInsertTabs(tabs = userInsertTabs, onClick = { viewModel.setState(it)})

            state?.message?.let { errorMessage ->
                Text(text = errorMessage, color = Color.Red)
            }

            when (state!!.loginOrRegister) {
                LoginOrRegister.LOGIN -> Login(onClick = { username, password ->
                    viewModel.login(username, password)
                })

                LoginOrRegister.REGISTER -> Register(onClick = { email, username, password, confirmPassword ->
                    viewModel.register(UserEntity(email, username, password), confirmPassword)
                })
            }
        }
    }
}
@Composable
fun Login(onClick: (String, String) -> Unit, modifier: Modifier = Modifier) {
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Column (
        modifier = modifier,
    ){
        TextField (
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier.height(8.dp))
        TextField (
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier.height(8.dp))
        Button(onClick = { onClick(username, password) }) {
            Text("Login")
        }
    }
}
@Composable
fun Register(onClick:(String, String, String, String) -> Unit, modifier: Modifier = Modifier) {
    var email by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }

    Column (
        modifier = modifier,
    ){
        TextField (
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier.height(8.dp))
        TextField (
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier.height(8.dp))
        TextField (
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier.height(8.dp))
        TextField (
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier.height(8.dp))
        Button(onClick = { onClick(email, username, password, confirmPassword) }) {
            Text("Register")
        }
    }
}

val userInsertTabs: List<UserInsertTabData> = listOf(
    UserInsertTabData(LoginOrRegister.LOGIN),
    UserInsertTabData(LoginOrRegister.REGISTER)
)
data class UserInsertTabData(
    val name: LoginOrRegister,
)

@Composable
fun UserInsertTabs(tabs: List<UserInsertTabData>, onClick: (LoginOrRegister) -> Unit, modifier: Modifier = Modifier){
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
        tabs.forEachIndexed { index, item ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = {
                    selectedTabIndex = index
                    onClick(item.name)
                },
                text = {
                    Text(text = item.name.toString())
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen(){
    LazyColumn {
        item {
            UserInsertTabs(tabs = userInsertTabs, onClick = {})
        }
        item {
            Login(onClick = { username, password ->})
        }
        item{
            Register(onClick = { email, name, passwd, confirmpasswd ->})
        }
    }
}
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val auth = remember { Firebase.auth }
        var firebaseUser: FirebaseUser? by remember { mutableStateOf(null) }
        var userEmail by remember { mutableStateOf("") }
        var userPassword by remember { mutableStateOf("") }

        if (firebaseUser == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = userEmail,
                    onValueChange = { userEmail = it },
                    placeholder = {
                        Text(
                            text = "Email Address"
                        )
                    }
                )
                TextField(
                    value = userPassword,
                    onValueChange = { userPassword = it },
                    placeholder = {
                        Text(
                            text = "Password"
                        )
                    },
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    onClick = {
                        scope.launch {
                            firebaseUser = try {
                                auth.createUserWithEmailAndPassword(
                                    email = userEmail,
                                    password = userPassword,
                                )
                                auth.currentUser
                            } catch (e: Exception) {
                                auth.signInWithEmailAndPassword(
                                    email = userEmail,
                                    password = userPassword,
                                )
                                auth.currentUser
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Sign in"
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = firebaseUser?.uid ?: "Unknown ID"
                )
                Button(
                    onClick = {
                        scope.launch {
                            auth.signOut()
                            firebaseUser = auth.currentUser
                        }
                    }
                ) {
                    Text(
                        text = "Sign out"
                    )
                }
            }
        }
    }
}
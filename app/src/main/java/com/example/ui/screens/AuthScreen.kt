package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SphereGradientBrush
import com.example.ui.theme.SphereBlue
import com.example.ui.theme.SpherePurple
import com.example.ui.viewmodels.SphereViewModel

@Composable
fun AuthScreen(viewModel: SphereViewModel) {
    var emailInput by remember { mutableStateOf("alex@sphere.app") }
    var passwordInput by remember { mutableStateOf("password123") }
    val show2FADialog by viewModel.show2FADialog.collectAsState()
    val twoFactorCode by viewModel.twoFactorCodeInput.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Branding Logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(SphereGradientBrush()),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = "Sphere Logo",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sphere",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Secure • Fast • AI-Powered Social Network",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Fields Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Sign In to Your Account",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_email_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_password_input")
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.performLogin(emailInput, "Email") },
                        colors = ButtonDefaults.buttonColors(containerColor = SpherePurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("auth_login_btn")
                    ) {
                        Text(
                            text = "Sign In",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Social Providers Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProviderChip(label = "Google", icon = Icons.Default.GMobiledata) {
                            viewModel.performLogin("google_user@gmail.com", "Google")
                        }
                        ProviderChip(label = "Facebook", icon = Icons.Default.Facebook) {
                            viewModel.performLogin("fb_user@facebook.com", "Facebook")
                        }
                        ProviderChip(label = "Apple", icon = Icons.Default.PhoneIphone) {
                            viewModel.performLogin("apple_user@icloud.com", "Apple")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Guest Mode Link
            TextButton(
                onClick = { viewModel.loginAsGuest() },
                modifier = Modifier.testTag("auth_guest_btn")
            ) {
                Text(
                    text = "Continue in Guest Mode →",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = SphereBlue
                )
            }
        }

        // 2FA Dialog
        if (show2FADialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Two-Factor Authentication (2FA)") },
                text = {
                    Column {
                        Text("Please enter the 6-digit verification code sent to your authenticator app:")
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = twoFactorCode,
                            onValueChange = { viewModel.twoFactorCodeInput.value = it },
                            label = { Text("Security Code") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("2fa_code_input")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.verify2FA() },
                        colors = ButtonDefaults.buttonColors(containerColor = SpherePurple)
                    ) {
                        Text("Verify & Continue")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.show2FADialog.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ProviderChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}

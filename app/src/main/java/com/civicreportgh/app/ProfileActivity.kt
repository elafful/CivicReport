package com.civicreportgh.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.CircleCropTransformation
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.lifecycleScope
import com.civicreportgh.app.databinding.ActivityProfileBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val auth = Firebase.auth
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        credentialManager = CredentialManager.create(this)

        setupUI()
        updateProfile()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.buttonSignIn.setOnClickListener {
            startGoogleSignIn()
        }

        binding.buttonSignOut.setOnClickListener {
            auth.signOut()
            updateProfile()
        }
    }

    private fun startGoogleSignIn() {
        val serverClientId = getString(R.string.default_web_client_id)
        if (serverClientId == "YOUR_WEB_CLIENT_ID_HERE") {
            Toast.makeText(this, "Please configure Web Client ID in strings.xml", Toast.LENGTH_LONG).show()
        }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts = false)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(autoSelectEnabled = true)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = this@ProfileActivity,
                    request = request,
                )
                handleSignIn(result)
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, getString(R.string.profile_sign_in_failed, e.message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        if ((credential is CustomCredential) && (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
            } catch (e: GoogleIdTokenParsingException) {
                Toast.makeText(this, "Google ID Token parsing failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Unexpected credential type", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateProfile()
                } else {
                    Toast.makeText(this, getString(R.string.profile_firebase_auth_failed), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateProfile() {
        val user = auth.currentUser
        if ((user != null) && !user.isAnonymous) {
            binding.textUserName.text = user.displayName ?: getString(R.string.profile_user_default)
            binding.textUserEmail.text = user.email
            binding.buttonSignIn.visibility = View.GONE
            binding.buttonSignOut.visibility = View.VISIBLE
            
            binding.imageProfile.load(user.photoUrl) {
                crossfade(enable = true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_profile)
            }
        } else {
            binding.textUserName.text = getString(R.string.profile_guest_user)
            binding.textUserEmail.text = getString(R.string.profile_not_signed_in)
            binding.buttonSignIn.visibility = View.VISIBLE
            binding.buttonSignOut.visibility = View.GONE
            binding.imageProfile.setImageResource(R.drawable.ic_profile)
        }
    }
}

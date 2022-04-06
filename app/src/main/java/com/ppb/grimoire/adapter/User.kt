package com.ppb.grimoire.adapter

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var personName : String,
    var personGivenName : String,
    var personFamilyName : String,
    var personEmail : String,
    var personId : String,
    var personPhoto: Uri?
) : Parcelable

package hu.tsukiakari.xiv.utility

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("DiscouragedApi")
fun matchJobToResource(ctx: Context, job: String) =
    ctx.resources.getIdentifier(
        // Blue Mage (Limited Class) is a special case
        if (job.contains("Blue Mage")) { "bluemage" }
        else { job.filterNot{ it.isWhitespace() }.lowercase() },
        "drawable", ctx.packageName
    )
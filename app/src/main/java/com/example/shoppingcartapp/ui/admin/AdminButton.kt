package com.example.shoppingcartapp.ui.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shoppingcartapp.R

@Composable
fun AdminToggleButton(
    isAdmin: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = if(isAdmin) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
            .padding(8.dp)
            .scale(0.5f)
    ) {
        Icon(
            imageVector = Icons.Default.AdminPanelSettings,
            contentDescription = stringResource(R.string.toggle_admin)
        )
    }
}
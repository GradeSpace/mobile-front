package org.example.project.core.presentation.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.add_attachment
import mobile_front.composeapp.generated.resources.camera
import mobile_front.composeapp.generated.resources.file
import mobile_front.composeapp.generated.resources.gallery
import mobile_front.composeapp.generated.resources.i24_add_a_photo
import mobile_front.composeapp.generated.resources.i24_attach_file
import mobile_front.composeapp.generated.resources.i24_photo_library
import mobile_front.composeapp.generated.resources.select_document
import mobile_front.composeapp.generated.resources.select_image
import mobile_front.composeapp.generated.resources.take_photo
import org.example.project.Platform
import org.example.project.core.presentation.AttachmentSource
import org.example.project.getPlatform
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentSourceBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSourceSelected: (AttachmentSource) -> Unit
) {
    if (isVisible) {
        val sheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.add_attachment),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AttachmentSourceItem(
                    icon = vectorResource(Res.drawable.i24_attach_file),
                    title = stringResource(Res.string.file),
                    description = stringResource(Res.string.select_document),
                    onClick = { onSourceSelected(AttachmentSource.FILE) }
                )

                if (getPlatform() != Platform.Desktop) {
                    Spacer(modifier = Modifier.height(8.dp))

                    AttachmentSourceItem(
                        icon = vectorResource(Res.drawable.i24_photo_library),
                        title = stringResource(Res.string.gallery),
                        description = stringResource(Res.string.select_image),
                        onClick = { onSourceSelected(AttachmentSource.GALLERY) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    AttachmentSourceItem(
                        icon = vectorResource(Res.drawable.i24_add_a_photo),
                        title = stringResource(Res.string.camera),
                        description = stringResource(Res.string.take_photo),
                        onClick = { onSourceSelected(AttachmentSource.CAMERA) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AttachmentSourceItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

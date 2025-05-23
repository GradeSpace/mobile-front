package org.example.project.core.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.attachments
import mobile_front.composeapp.generated.resources.i24_docs_outline
import mobile_front.composeapp.generated.resources.i24_draft_outline
import mobile_front.composeapp.generated.resources.i24_image_outline
import mobile_front.composeapp.generated.resources.i24_pdf_outline
import mobile_front.composeapp.generated.resources.press_to_open
import org.example.project.core.data.model.Action
import org.example.project.core.data.model.attachment.Attachment
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EventAttachments(
    attachments: List<Attachment>,
    modifier: Modifier = Modifier
) {
    if (attachments.isEmpty()) return

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .wrapContentSize()
                .wrapContentHeight()
        ) {
            Text(
                text = stringResource(Res.string.attachments),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp, start = 8.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                attachments.forEach { attachment ->
                    Attachment(
                        attachment = attachment,
                        modifier = Modifier
                            .widthIn(min = 300.dp, max = 500.dp)
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Attachment(
    attachment: Attachment,
    onClick: Action = {},
    modifier: Modifier = Modifier
) {
    val fileType = when (attachment) {
        is Attachment.FileAttachment -> attachment.fileType
    }

    val icon = when (fileType) {
        Attachment.FileType.IMAGE -> vectorResource(Res.drawable.i24_image_outline)
        Attachment.FileType.PDF -> vectorResource(Res.drawable.i24_pdf_outline)
        Attachment.FileType.WORD -> vectorResource(Res.drawable.i24_docs_outline)
        Attachment.FileType.UNKNOWN -> vectorResource(Res.drawable.i24_draft_outline)
    }

    val fileSize = formatFileSize(attachment.fileSize)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f)
        ),
        modifier = modifier
            .clip(
                RoundedCornerShape(16.dp)
            )
            .clickable {
                onClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = attachment.fileName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$fileSize • ${stringResource(Res.string.press_to_open)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun formatFileSize(sizeInBytes: Long): String {
    return when {
        sizeInBytes < 1024 -> "$sizeInBytes B"
        sizeInBytes < 1024 * 1024 -> "${sizeInBytes / 1024} KB"
        sizeInBytes < 1024 * 1024 * 1024 -> "${sizeInBytes / (1024 * 1024)} MB"
        else -> "${sizeInBytes / (1024 * 1024 * 1024)} GB"
    }
}


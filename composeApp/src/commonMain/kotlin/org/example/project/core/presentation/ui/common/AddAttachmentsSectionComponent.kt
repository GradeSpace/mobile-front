package org.example.project.core.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.add_attachment
import mobile_front.composeapp.generated.resources.attachments
import mobile_front.composeapp.generated.resources.remove_attachment
import org.example.project.core.data.model.attachment.Attachment
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddAttachmentsSectionComponent(
    attachments: List<Attachment>,
    onAddAttachment: () -> Unit,
    onRemoveAttachment: (Attachment) -> Unit,
    onAttachmentClick: (Attachment) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.attachments),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (attachments.isNotEmpty()) {
            val imageAttachments = attachments.filter {
                it is Attachment.FileAttachment && it.fileType == Attachment.FileType.IMAGE
            }
            val fileAttachments = attachments.filter {
                it is Attachment.FileAttachment && it.fileType != Attachment.FileType.IMAGE
            }

            if (imageAttachments.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(imageAttachments) { attachment ->
                        Box {
                            AsyncImage(
                                model = attachment.url,
                                contentDescription = attachment.fileName,
                                contentScale = ContentScale.Crop,
                                modifier = modifier
                                    .padding(vertical = 4.dp)
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onAttachmentClick(attachment) },
                            )

                            IconButton(
                                onClick = { onRemoveAttachment(attachment) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = stringResource(Res.string.remove_attachment)
                                )
                            }
                        }
                    }
                }
            }

            // Отображаем файлы
            if (fileAttachments.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(fileAttachments) { attachment ->
                        Box {
                            Attachment(
                                attachment = attachment,
                                onClick = {
                                    onAttachmentClick(attachment)
                                },
                                modifier = Modifier
                                    .widthIn(min = 300.dp, max = 500.dp)
                                    .padding(vertical = 4.dp)

                            )

                            IconButton(
                                onClick = { onRemoveAttachment(attachment) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = stringResource(Res.string.remove_attachment)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onAddAttachment,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.add_attachment))
        }
    }
}

/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.affirmations

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.affirmations.data.Datasource
import com.example.affirmations.model.Affirmation
import com.example.affirmations.ui.theme.AffirmationsTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AffirmationsTheme {
        AffirmationApp()
      }
    }
  }
}

@Composable
fun  AffirmationApp() {
  val context = LocalContext.current
  AffirmationsTheme {
    AffirmationList(affirmationList = Datasource().loadAffirmations())
  }}

@Composable
private fun AffirmationList(affirmationList: List<Affirmation>, modifier: Modifier = Modifier) {
  LazyColumn {
    items(affirmationList){ affirmation ->
      AffirmationCard(affirmation)
    }
  }
}

@Composable
fun AffirmationCard(affirmation: Affirmation, modifier: Modifier = Modifier) {
  var expanded by remember { mutableStateOf(false) }
  Card(
    modifier = Modifier.padding(8.dp),
    elevation = 4.dp
  ) {
    Column (
      modifier = Modifier
        .animateContentSize(
          animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
          )
        )
    ) {
      Image(
        painter = painterResource(affirmation.imageResourceId),
        contentDescription = stringResource(affirmation.stringResourceId),
        modifier = Modifier
          .fillMaxWidth()
          .height(194.dp),
        contentScale = ContentScale.Crop
      )
      Text(
        text = stringResource(affirmation.stringResourceId),
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.h6
      )
      //Spacer(modifier = Modifier.weight(1f))
      ExpandItemButton(
        expanded = expanded,
        onClick = { expanded = !expanded}
      )
      if (expanded) {
        DescriptionResource(affirmation.stringResourceId)
      }
    }
  }
}

@Preview
@Composable
private fun AffirmationCardPreview() {
  AffirmationCard(Affirmation(R.string.affirmation1, R.string.expand_button_description, R.drawable.image1))
}

@Composable
private fun ExpandItemButton(
  expanded: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
){
  IconButton(onClick = onClick){
    Icon(
      imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
      tint = MaterialTheme.colors.secondary,
      contentDescription = stringResource(id = R.string.expand_button_description)
    )
  }
}

@Composable
fun DescriptionResource(@StringRes description: Int, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier.padding(
      start = 16.dp,
      top = 8.dp,
      bottom = 16.dp,
      end = 16.dp
    )
  ) {
    Text(
      text = stringResource(R.string.about),
      style = MaterialTheme.typography.h3,
    )
    Text(
      text = stringResource(description),
      style = MaterialTheme.typography.body1,
    )
  }
}
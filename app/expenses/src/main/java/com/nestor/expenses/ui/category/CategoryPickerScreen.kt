package com.nestor.expenses.ui.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.subcategory.SubcategoryWithCategories
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.list.SYItemIcon
import com.nestor.uikit.list.SYListItemData
import com.nestor.uikit.theme.image.LocalSYImageServerProvider
import com.nestor.uikit.theme.spacing.LocalSYPadding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryPickerSheet(
    sheetState: SheetState = rememberModalBottomSheetState(),
    categories: StateFlow<ResponseWrapper<List<SubcategoryWithCategories>>>,
    onCategorySelected: (CategoryEntity) -> Unit,
    onDismissRequest: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {
        CategoryPickerDialogContent(
            modifier = Modifier.padding(LocalSYPadding.current.screenHorizontalPadding),
            categories = categories.collectAsState().value,
            onCategorySelected = {
                onCategorySelected(it)
                coroutineScope.launch { sheetState.hide() }
                onDismissRequest()
            }
        )
    }
}

@Composable
private fun CategoryPickerDialogContent(
    modifier: Modifier = Modifier,
    categories: ResponseWrapper<List<SubcategoryWithCategories>>,
    onCategorySelected: (CategoryEntity) -> Unit,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (categories.isLoading) {
            Text(text = "Loading")
        }
        categories.body?.let {
            it.forEach { subcat ->
                Text(text = subcat.subCategory.name)
                LazyRow {
                    val categoryEntities = subcat.categories
                    items(categoryEntities.size) { index ->
                        val cat = categoryEntities[index]
                        CategoryView(
                            item = cat,
                            modifier = Modifier
                                .width(95.dp)
                                .clickable { onCategorySelected(cat) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryView(
    modifier: Modifier = Modifier,
    item: CategoryEntity
) {
    val painter =
        rememberAsyncImagePainter(model = item.iconUrl(LocalSYImageServerProvider.current))
    val tint = if (item.tint != null) Color(item.tint!!) else MaterialTheme.colorScheme.primary
    val icon = remember {
        SYListItemData.SYListItemIcon(tint = tint, icon = painter)
    }
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SYItemIcon(
            icon = icon
        )
        Text(
            text = item.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun CategoryPickerSheetPreview() {
    SpentifyTheme {
        Scaffold {
            CategoryPickerDialogContent(
                modifier = Modifier.padding(it),
                categories = ResponseWrapper.loading(),
                onCategorySelected = {}
            )
        }
    }
}
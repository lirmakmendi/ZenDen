package com.sceproject.zenden.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.drawer.DrawerItems
import com.sceproject.zenden.data.viewmodels.HomeViewModel
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter
import com.sceproject.zenden.theme.BgColor
import com.sceproject.zenden.theme.GrayColor
import com.sceproject.zenden.theme.Primary
import com.sceproject.zenden.theme.Secondary
import com.sceproject.zenden.theme.TextColor
import com.sceproject.zenden.theme.WhiteColor
import com.sceproject.zenden.theme.componentShapes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun <T : ViewModel> AppScaffold(
    title: String,
    homeViewModel: HomeViewModel,
    secondaryViewModel: T? = null,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable (PaddingValues) -> Unit
) {
    LaunchedEffect(key1 = true) {
        homeViewModel.getUserData()
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppToolbar(
                toolbarTitle = title,
                logoutButtonClicked = {
                    homeViewModel.logout()
                },
                navigationIconClicked = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            NavigationDrawerHeader(("שלום " + homeViewModel.firstName.value) ?: "")
            NavigationDrawerBody(
                navigationDrawerItems = DrawerItems.drawerItemsList,
                onNavigationItemClicked = { item ->
                    when (item.itemId) {
                        "dashboardScreen" -> ZenDenAppRouter.navigateTo(Screen.HomeScreen)
                        "relaxationTechniquesScreen" -> ZenDenAppRouter.navigateTo(Screen.RelaxationScreen)
                        "anxietyTrendsScreen" -> ZenDenAppRouter.navigateTo(Screen.AnxietyTrendsScreen)
                        "AnswerPDSSScreen" -> ZenDenAppRouter.navigateTo(Screen.AnswerPDSSScreen)
                        "myProfileScreen" -> ZenDenAppRouter.navigateTo(Screen.MyProfileScreen)
                        "panicAttackInfoScreen" -> ZenDenAppRouter.navigateTo(Screen.PanicAttackInfoScreen)
                        "CheckPanicAttackScreen" -> ZenDenAppRouter.navigateTo(Screen.CheckPanicAttackScreen)
                        // Handle other cases as needed
                    }
                }
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
fun <T : ViewModel> AdminScaffold(
    title: String,
    homeViewModel: HomeViewModel,
    secondaryViewModel: T? = null,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable (PaddingValues) -> Unit
) {
    LaunchedEffect(key1 = true) {
        homeViewModel.getUserData()
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppToolbar(
                toolbarTitle = title,
                logoutButtonClicked = {
                    homeViewModel.logout()
                },
                navigationIconClicked = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            NavigationDrawerHeader(("שלום " + homeViewModel.firstName.value) ?: "")
            NavigationDrawerBody(
                navigationDrawerItems = listOf(
                    DrawerItems(
                        title = "מסך הבית",
                        icon = Icons.Default.Dashboard,
                        description = "Dashboard",
                        itemId = "AdminDashboardScreen"
                    ),
                    DrawerItems(
                        title = "סטאטוס DB",
                        icon = Icons.Default.Dashboard,
                        description = "DatabaseStatusScreen",
                        itemId = "DatabaseStatusScreen"
                    ),
                    DrawerItems(
                        title = "ניהול משתמשים",
                        icon = Icons.Default.Dashboard,
                        description = "ManageUsersScreen",
                        itemId = "ManageUsersScreen"
                    ),
                ),
                onNavigationItemClicked = { item ->
                    when (item.itemId) {
                        "AdminDashboardScreen" -> ZenDenAppRouter.navigateTo(Screen.AdminScreen)
                        "ManageUsersScreen" -> ZenDenAppRouter.navigateTo(Screen.ManageUsersScreen)
                        "DatabaseStatusScreen" -> ZenDenAppRouter.navigateTo(Screen.DatabaseStatusScreen)
                        // Handle other admin-specific cases as needed
                    }
                }
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}


@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ), color = colorResource(id = R.color.colorText),
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ), color = colorResource(id = R.color.colorText),
        textAlign = TextAlign.Center
    )
}

@Composable
fun MyTextFieldComponent(
    labelValue: String, painterResource: Painter,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false
) {

    val textValue = remember {
        mutableStateOf("")
    }
    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShapes.small),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
            backgroundColor = BgColor
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextChanged(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        isError = !errorStatus
    )
}


@Composable
fun PasswordTextFieldComponent(
    labelValue: String, painterResource: Painter,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false
) {

    val localFocusManager = LocalFocusManager.current
    val password = remember {
        mutableStateOf("")
    }

    val passwordVisible = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShapes.small),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
            backgroundColor = BgColor
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        maxLines = 1,
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        trailingIcon = {

            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            val description = if (passwordVisible.value) {
                stringResource(id = R.string.hide_password)
            } else {
                stringResource(id = R.string.show_password)
            }

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }

        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = !errorStatus
    )
}

@Composable
fun CheckboxComponent(
    value: String,
    onTextSelected: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val checkedState = remember {
            mutableStateOf(false)
        }

        Checkbox(checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = !checkedState.value
                onCheckedChange.invoke(it)
            })

        ClickableTextComponent(value = value, onTextSelected)
    }
}

@Composable

fun ClickableTextComponent(value: String, onTextSelected: (String) -> Unit) {
    val initialText = "על ידי השימוש אתה מקבל את "
    val privacyPolicyText = "מדיניות הפרטיות"
    val andText = " ואת "
    val termsAndConditionsText = "תנאי השימוש"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        append(andText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = termsAndConditionsText, annotation = termsAndConditionsText)
            append(termsAndConditionsText)
        }
    }

    ClickableText(text = annotatedString, onClick = { offset ->

        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.also { span ->
                Log.d("ClickableTextComponent", "{${span.item}}")

                if ((span.item == termsAndConditionsText) || (span.item == privacyPolicyText)) {
                    onTextSelected(span.item)
                }
            }

    })
}

@Composable
fun ButtonComponent(value: String, onButtonClicked: () -> Unit, isEnabled: Boolean = false) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Secondary, Primary)),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }

    }
}

@Composable
fun DividerTextComponent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.or),
            fontSize = 18.sp,
            color = TextColor
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp
        )
    }
}

@Composable
fun ClickableLoginTextComponent(tryingToLogin: Boolean = true, onTextSelected: (String) -> Unit) {
    val initialText =
        if (tryingToLogin) "כבר יש לך משתמש? " else "אין לך משתמש עדיין? "
    val loginText = if (tryingToLogin) "התחברות" else "הרשמה"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{${span.item}}")

                    if (span.item == loginText) {
                        onTextSelected(span.item)
                    }
                }

        },
    )
}

@Composable
fun UnderLinedTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ), color = colorResource(id = R.color.colorGray),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )

}

@Composable
fun AppToolbar(
    toolbarTitle: String, logoutButtonClicked: () -> Unit,
    navigationIconClicked: () -> Unit
) {

    TopAppBar(
        backgroundColor = Primary,
        title = {
            Text(
                text = toolbarTitle, color = WhiteColor
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navigationIconClicked.invoke()
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = WhiteColor
                )
            }

        },
        actions = {
            IconButton(onClick = {
                logoutButtonClicked.invoke()
            }) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = stringResource(id = R.string.logout),
                )
            }
        }
    )
}

@Composable
fun NavigationDrawerHeader(value: String?) {
    Box(
        modifier = Modifier
            .background(
                Brush.horizontalGradient(
                    listOf(MaterialTheme.colors.primary, MaterialTheme.colors.secondary)
                )
            )
            .fillMaxWidth()
            .height(180.dp)
            .padding(32.dp),
        contentAlignment = Alignment.BottomStart // Aligns the text to the bottom-start
    ) {
        Text(
            text = value ?: stringResource(R.string.navigation_header),
            style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.onSurface), // Ensures legibility
            fontSize = 28.sp
        )
    }
}

@Composable
fun NavigationDrawerBody(
    navigationDrawerItems: List<DrawerItems>,
    onNavigationItemClicked: (DrawerItems) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(navigationDrawerItems) { item ->
            NavigationItemRow(item = item, onNavigationItemClicked = onNavigationItemClicked)
        }
    }
}


@Composable
fun NavigationItemRow(
    item: DrawerItems,
    onNavigationItemClicked: (DrawerItems) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigationItemClicked(item) }
            .padding(vertical = 12.dp, horizontal = 16.dp), // Balanced padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.description,
            tint = MaterialTheme.colors.onSurface // Icon color
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.body2, // Consistent with Material Design
            color = MaterialTheme.colors.onSurface // Text color
        )
    }
}


@Composable
fun NavigationDrawerText(
    title: String,
    textSize: TextUnit,
    textColor: Color = MaterialTheme.colors.onSurface
) {
    Text(
        text = title,
        color = textColor,
        fontSize = textSize,
        modifier = Modifier.padding(
            vertical = 8.dp,
            horizontal = 16.dp
        ), // Added padding for better spacing
        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Medium) // Use Material Theme typography for consistency
    )
}


@Composable
fun TermsContent() {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Text(text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(lineHeight = 24.sp)) {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("1. איסוף נתונים ושימוש\n")
                }
                append("על ידי שימוש באפליקציה זו, אתה מסכים לאיסוף ושימוש במידע מחיישני השעון של Wear OS שלך כדי לנתח ולזהות דפוסים המעידים על רמות חרדה.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("2. מידע בריאותי\n")
                }
                append("אפליקציה זו אינה מהווה תחליף לייעוץ רפואי, אבחון או טיפול מקצועי. פנה תמיד לייעוץ של הרופא שלך או ספק בריאות מוסמך אחר בכל שאלה שיש לך בנוגע למצב רפואי.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("3. הסכמה וביטול\n")
                }
                append("הסכמה לאיסוף וניתוח נתונים ניתנת לביטול בכל עת דרך הגדרות האפליקציה. עם זאת, ביטול ההסכמה יביא לאובדן הפונקציונליות של האפליקציה.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("4. אבטחת נתונים\n")
                }
                append("אנו מיישמים מגוון אמצעי אבטחה כדי לשמור על בטיחות המידע האישי שלך כאשר אתה מזין, שולח או ניגש למידע האישי שלך.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("5. שינויים בתנאים\n")
                }
                append("אנו שומרים לעצמנו את הזכות לשנות תנאים אלה בכל עת. המשך השימוש שלך באפליקציה בעקבות שינויים כלשהם פירושו שאתה מקבל את השינויים הללו.\n\n")
            }
        }, style = TextStyle(lineHeight = 20.sp))
    }
}


@Composable
fun <T : ViewModel> OfflineAppScaffold(
    title: String,
    homeViewModel: HomeViewModel,
    secondaryViewModel: T? = null,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    enableMenu: Boolean = true,
    onBackClicked: () -> Unit = { ZenDenAppRouter.navigateTo(Screen.LoginScreen) },
    content: @Composable (PaddingValues) -> Unit
) {
    LaunchedEffect(key1 = true) {
        if (enableMenu) {
            homeViewModel.getUserData()
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (enableMenu) {
                AppToolbar(
                    toolbarTitle = title,
                    logoutButtonClicked = {
                        homeViewModel.logout()
                    },
                    navigationIconClicked = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                )
            } else {
                OfflineAppToolbar(
                    toolbarTitle = title,
                    onBackClicked = onBackClicked
                )
            }
        },
        drawerGesturesEnabled = enableMenu && scaffoldState.drawerState.isOpen,
        drawerContent = if (enableMenu) {
            {
                NavigationDrawerHeader(("שלום " + homeViewModel.firstName.value) ?: "")
                NavigationDrawerBody(
                    navigationDrawerItems = DrawerItems.drawerItemsList,
                    onNavigationItemClicked = { item ->
                        when (item.itemId) {
                            "dashboardScreen" -> ZenDenAppRouter.navigateTo(Screen.HomeScreen)
                            "relaxationTechniquesScreen" -> ZenDenAppRouter.navigateTo(Screen.RelaxationScreen)
                            "anxietyTrendsScreen" -> ZenDenAppRouter.navigateTo(Screen.AnxietyTrendsScreen)
                            "AnswerPDSSScreen" -> ZenDenAppRouter.navigateTo(Screen.AnswerPDSSScreen)
                            "myProfileScreen" -> ZenDenAppRouter.navigateTo(Screen.MyProfileScreen)
                            "panicAttackInfoScreen" -> ZenDenAppRouter.navigateTo(Screen.PanicAttackInfoScreen)
                            "CheckPanicAttackScreen" -> ZenDenAppRouter.navigateTo(Screen.CheckPanicAttackScreen)
                            // Handle other cases as needed
                        }
                    }
                )
            }
        } else {
            null
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}


@Composable
fun OfflineAppToolbar(
    toolbarTitle: String,
    onBackClicked: () -> Unit = { ZenDenAppRouter.navigateTo(Screen.LoginScreen) }
) {
    TopAppBar(
        title = { Text(text = toolbarTitle) },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "חזור")
            }
        }
    )
}

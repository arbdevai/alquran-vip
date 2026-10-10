sed -i '/@Composable/,$d' app/src/main/java/com/arbdevai/quranvip/ui/QuranApp.kt
cat << 'INNER_EOF' >> app/src/main/java/com/arbdevai/quranvip/ui/QuranApp.kt
@Composable
private fun FloatingIslandNavbar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.padding(horizontal = 24.dp),
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFFFAFAFA).copy(alpha = 0.95f),
            shadowElevation = 12.dp,
            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
        ) {
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val navItems = listOf(
                    AppScreen.HOME to Icons.Default.Home,
                    AppScreen.QURAN to Icons.Default.MenuBook,
                    AppScreen.PRAYER to Icons.Default.AccessTime,
                    AppScreen.CALENDAR to Icons.Default.CalendarMonth,
                    AppScreen.TASBIH to Icons.Default.TouchApp
                )

                navItems.forEach { (screen, icon) ->
                    val selected = currentScreen == screen
                    val tint by animateColorAsState(if (selected) Color(0xFF0F172A) else Color(0xFF94A3B8), label = "tint")
                    val bgAlpha by animateFloatAsState(if (selected) 0.1f else 0f, label = "bg")
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onScreenSelected(screen) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFF0F172A).copy(alpha = bgAlpha)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
        }
    }
}
INNER_EOF

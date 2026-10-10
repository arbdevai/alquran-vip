sed -i '/val quotes = remember {/,+40c\
            val quotes = remember {\
                listOf(\
                    Triple("اَلَا بِذِكْرِ اللّٰهِ تَطْمَىِٕنُّ الْقُلُوْبُ", "Ingatlah, hanya dengan mengingat Allah hati menjadi tenteram.", "QS. Ar-Ra'\''d: 28"),\
                    Triple("فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ", "Maka nikmat Tuhanmu yang manakah yang kamu dustakan?", "QS. Ar-Rahman: 13"),\
                    Triple("إِنَّ مَعَ الْعُسْرِ يُسْرًا", "Sesungguhnya sesudah kesulitan itu ada kemudahan.", "QS. Al-Insyirah: 6"),\
                    Triple("لَا تَحْزَنْ إِنَّ اللَّهَ مَعَنَا", "Janganlah engkau bersedih, sesungguhnya Allah bersama kita.", "QS. At-Taubah: 40"),\
                    Triple("وَهُوَ مَعَكُمْ أَيْنَ مَا كُنتُمْ", "Dan Dia bersama kamu di mana saja kamu berada.", "QS. Al-Hadid: 4"),\
                    Triple("وَاللَّهُ يَعْلَمُ وَأَنتُمْ لَا تَعْلَمُونَ", "Allah mengetahui, sedang kamu tidak mengetahui.", "QS. Al-Baqarah: 216"),\
                    Triple("فَاصْبِرْ صَبْرًا جَمِيلًا", "Maka bersabarlah dengan kesabaran yang baik.", "QS. Al-Ma'\''arij: 5"),\
                    Triple("وَقُل رَّبِّ زِدْنِي عِلْمًا", "Dan katakanlah: Ya Tuhanku, tambahkanlah kepadaku ilmu.", "QS. Taha: 114"),\
                    Triple("إِنَّ اللَّهَ يُحِبُّ الْمُتَوَكِّلِينَ", "Sesungguhnya Allah menyukai orang-orang yang bertawakal kepada-Nya.", "QS. Ali '\''Imran: 159"),\
                    Triple("وَمَا تَوْفِيقِي إِلَّا بِاللَّهِ", "Dan tidak ada taufik bagiku melainkan dengan (pertolongan) Allah.", "QS. Hud: 88"),\
                    Triple("حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ", "Cukuplah Allah menjadi Penolong kami dan Allah adalah sebaik-baik Pelindung.", "QS. Ali '\''Imran: 173"),\
                    Triple("وَرَحْمَتِي وَسِعَتْ كُلَّ شَيْءٍ", "Dan rahmat-Ku meliputi segala sesuatu.", "QS. Al-A'\''raf: 156"),\
                    Triple("إِنَّ اللَّهَ مَعَ الصَّابِرِينَ", "Sesungguhnya Allah beserta orang-orang yang sabar.", "QS. Al-Baqarah: 153"),\
                    Triple("فَاذْكُرُونِي أَذْكُرْكُمْ", "Karena itu, ingatlah kamu kepada-Ku niscaya Aku ingat (pula) kepadamu.", "QS. Al-Baqarah: 152"),\
                    Triple("ادْعُونِي أَسْتَجِبْ لَكُمْ", "Berdoalah kepada-Ku, niscaya akan Kuperkenankan bagimu.", "QS. Ghafir: 60"),\
                    Triple("وَأَحْسِنُوا ۛ إِنَّ اللَّهَ يُحِبُّ الْمُحْسِنِينَ", "Dan berbuat baiklah, karena sesungguhnya Allah menyukai orang-orang yang berbuat baik.", "QS. Al-Baqarah: 195"),\
                    Triple("وَتَوَكَّلْ عَلَى اللَّهِ ۚ وَكَفَىٰ بِاللَّهِ وَكِيلًا", "Dan bertawakallah kepada Allah. Dan cukuplah Allah sebagai Pemelihara.", "QS. Al-Ahzab: 3")\
                )\
            }\
            val quote = remember { quotes.random() }\
            Text(quote.first, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = com.arbdevai.quranvip.ui.theme.LPMQ)\
            Spacer(Modifier.height(8.dp))\
            Text(quote.second, color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)\
            Spacer(Modifier.height(4.dp))\
            Text(quote.third, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)' app/src/main/java/com/arbdevai/quranvip/ui/screens/HomeScreen.kt

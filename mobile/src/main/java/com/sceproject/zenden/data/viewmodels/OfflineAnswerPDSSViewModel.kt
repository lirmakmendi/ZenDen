package com.sceproject.zenden.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OfflineAnswerPDSSViewModel : ViewModel() {

    private val _pdssResponses = MutableLiveData<Map<Int, Int>>(emptyMap())
    val pdssResponses: LiveData<Map<Int, Int>> = _pdssResponses

    private val _totalScore = MutableLiveData<Int>()
    val totalScore: LiveData<Int> = _totalScore

    val pdssQuestions = listOf(
        PdssQuestion(
            1, "כמה התקפי פאניקה היו לך בשבוע האחרון?",
            answers = listOf("ללא", "1-2", "3-4", "5-7", "8+"),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(
            2, "כמה מעיקים היו התקפי הפאניקה שחווית בשבוע האחרון?",
            answers = listOf(
                "לא מעיק בכלל",
                "מצוקה מתונה",
                "מצוקה בינונית",
                "מצוקה קשה",
                "מצוקה חמורה ביותר, כמעט תמידית"
            ),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(
            3,
            "בשבוע האחרון, עד כמה דאגת או חשת חרדה לגבי מתי התקף החרדה הבא שלך יתרחש או לגבי חשש ללקות בהתקף חרדה נוסף?",
            answers = listOf(
                "בכלל לא",
                "בצורה מתונה",
                "בצורה בינונית",
                "בצורה חמורה",
                "דאגה תמידית"
            ),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(
            4,
            "בשבוע האחרון, עד כמה נמנעת ממצבים, מקומות, פעילויות או אנשים בגלל פחדים מהתקף חרדה?",
            answers = listOf(
                "אין הימנעות",
                "הימנעות מתונה",
                "הימנעות בינונית",
                "הימנעות חמורה",
                "הימנעות מתמדת"
            ),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(
            5, "בשבוע האחרון, עד כמה התקפי החרדה הפריעו לחיי החברה/פעילויות הפנאי שלך?",
            answers = listOf("ללא", "בצורה מתונה", "בצורה בינונית", "בצורה חמורה", "הפרעה תמידית"),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(
            6,
            "בשבוע האחרון, עד כמה התקפי החרדה הפריעו ליכולת שלך לעבוד או לבצע את תחומי האחריות שלך בבית?",
            answers = listOf("ללא", "בצורה מתונה", "בצורה בינונית", "בצורה חמורה", "הפרעה תמידית"),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(
            7, "בשבוע האחרון, עד כמה התקפי הפאניקה הפריעו לחיי המשפחה/אחריות הביתית שלך?",
            answers = listOf("ללא", "בצורה מתונה", "בצורה בינונית", "בצורה חמורה", "הפרעה תמידית"),
            scores = listOf(0, 1, 2, 3, 4)
        )
    )

    fun setPdssResponse(questionId: Int, score: Int) {
        _pdssResponses.value = _pdssResponses.value?.toMutableMap()?.apply {
            put(questionId, score)
        }
    }

    fun calculateTotalScore() {
        _totalScore.value = _pdssResponses.value?.values?.sum() ?: 0
    }

    data class PdssQuestion(
        val id: Int,
        val questionText: String,
        val answers: List<String>,
        val scores: List<Int>
    )
}
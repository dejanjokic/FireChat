package hr.tvz.firechat.data.model

import timber.log.Timber

object EmotionMapper {

    fun getEmotion(smilingProbability: Float, leftEyeOpenProbability: Float, rightEyeOpenProbability: Float): ChatMessage.Emotion {

        Timber.d("Smiling probability: $smilingProbability")
        Timber.d("Left eye open probability: $leftEyeOpenProbability")
        Timber.d("Right eye open probability: $rightEyeOpenProbability")

        if (smilingProbability > 0.5) {

            // Smiling, both eyes open
            if (leftEyeOpenProbability > 0.5 && rightEyeOpenProbability > 0.5)
                return ChatMessage.Emotion.FACE1

            // Smiling, both eyes closed
            if (leftEyeOpenProbability < 0.5 && rightEyeOpenProbability < 0.5)
                return ChatMessage.Emotion.FACE2

            // Smiling, left eye open, right eye closed
            if (leftEyeOpenProbability > 0.5 && rightEyeOpenProbability < 0.5)
                return ChatMessage.Emotion.FACE3

            // Smiling, left eye closed, right eye open
            if (leftEyeOpenProbability < 0.5 && rightEyeOpenProbability > 0.5)
                return ChatMessage.Emotion.FACE4

        } else {

            // Not smiling, bot eyes open
            if (leftEyeOpenProbability > 0.5 && rightEyeOpenProbability > 0.5)
                return ChatMessage.Emotion.FACE5

            // Not smiling, both eyes closed
            if (leftEyeOpenProbability < 0.5 && rightEyeOpenProbability < 0.5)
                return ChatMessage.Emotion.FACE6

            // Not smiling, left eye open, right eye closed
            if (leftEyeOpenProbability > 0.5 && rightEyeOpenProbability < 0.5)
                return ChatMessage.Emotion.FACE7

            // Not smiling, left eye closed, right eye open
            if (leftEyeOpenProbability < 0.5 && rightEyeOpenProbability > 0.5)
                return ChatMessage.Emotion.FACE8
        }
        return ChatMessage.Emotion.FACE0
    }
}
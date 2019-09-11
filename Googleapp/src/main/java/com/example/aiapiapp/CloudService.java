package com.example.aiapiapp;

import ai.api.android.AIConfiguration;

public class CloudService {

    public AIConfiguration service()
    {
        final AIConfiguration config = new AIConfiguration(
                "122c0cd2679e42078de299ca7d273d21",
                ai.api.AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        return config;
    }

}

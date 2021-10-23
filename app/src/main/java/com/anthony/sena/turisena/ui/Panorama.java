package com.anthony.sena.turisena.ui;

import com.huawei.hms.panorama.PanoramaInterface;
import com.huawei.hms.panorama.e;

public final class Panorama {
    public static PanoramaInterface sInstance = new e();

    public Panorama() {
    }

    public static PanoramaInterface getInstance() {
        return sInstance;
    }
}
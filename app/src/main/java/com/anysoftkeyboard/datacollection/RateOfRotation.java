package com.anysoftkeyboard.datacollection;

public class RateOfRotation {
    private float ror_x;
    private float ror_y;
    private float ror_z;

    public RateOfRotation(float ror_x, float ror_y, float ror_z) {
        this.ror_x = ror_x;
        this.ror_y = ror_y;
        this.ror_z = ror_z;
    }

    public float getRor_x() {
        return ror_x;
    }

    public float getRor_y() {
        return ror_y;
    }

    public void setRor_y(float ror_y) {
        this.ror_y = ror_y;
    }

    public float getRor_z() {
        return ror_z;
    }

    public void setRor_z(float ror_z) {
        this.ror_z = ror_z;
    }

    public void setRor_x(float ror_x) {
        this.ror_x = ror_x;
    }

    @Override
    public String toString() {
        return "RateOfRotation{" +
                "ror_x=" + ror_x +
                ", ror_y=" + ror_y +
                ", ror_z=" + ror_z +
                '}';
    }
}

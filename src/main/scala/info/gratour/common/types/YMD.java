/*******************************************************************************
 *  Copyright (c) 2019, 2021 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package info.gratour.common.types;

import java.time.LocalDate;

public class YMD {

    private int year;
    private int month;
    private int day;

    public YMD() {
    }

    public YMD(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public YMD(LocalDate date) {
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.day = date.getDayOfMonth();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "YMD{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}

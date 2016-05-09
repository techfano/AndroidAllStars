/* The MIT License (MIT)
* Copyright (c) 2016 BELATRIX
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package com.belatrixsf.allstars.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by icerrate on 25/04/2016.
 */
public class Star {

    private Integer pk;
    private String date;
    @SerializedName("text")
    private String message;
    @SerializedName("from_user")
    private Employee fromUser;
    private Category category;
    private Keyword keyword;

    public Integer getPk() {
        return pk;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public Employee getFromUser() {
        return fromUser;
    }

    public Category getCategory() {
        return category;
    }

    public Keyword getKeyword() {
        return keyword;
    }
}
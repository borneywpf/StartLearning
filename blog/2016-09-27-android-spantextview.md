---
layout: post
title: "Android自定义控件之SpanTextView"
date: 2016-09-27 14:39:22 +0800
comments: true
categories: android自定义控件
published: true
---
<!--more-->
先看效果图<br>
![spantextview](/images/spantextview.png)<br>
是不是没有看出来什么，不要着急，往下看

## 概述
不可质疑，在android中的TextView是个非常强大的控件，作为一个几年的developer，还是对其知之甚少；最近公司的一个新项目中有这样一个gui需求，
在一行text的显示中显示几个标签，类似于效果图中的边框，要怎么实现呢；总不可能在添加一个TextView显示吧，首先因为标签个数不定，其次这样实现真的
很low，那么怎么办呢，我们肯定会想到android中的SpannableString，这个东西能通过设置span很好的渲染TextView中的局部内容，于是乎自己查了些资料
，对于我这种懒人，我被惊讶到了，我的天啊，怎么会有这么多的Span定义，有字体、颜色、背景色、大小等等。而且在使用的时候需要指定是什么位置
的text，为了以后工作方便，于是决定，进一步封装；效果和官方定义的效果差不多（如效果图），下面我们就介绍封装过程和使用方法。

## 优缺点
优点

> - 涵盖了大部分的Span
> - 可以方便对不同Span组合应用
> - 不需要去数对第几个字符设置效果
> - 添加了设置背景的功能
> - 可以方便设置自定义的Span

缺点

> - 和TextView结合不是很好，只能依赖append方法
> - 对固定的text引用不佳

## 实现源码
 - 集成自TextView并添加一个spanedable方法
 ```java
    public Spanedable spanedable(CharSequence text) {
        return new Spanedable(text);
    }
 ```

 - 添加内部类Spanedable,并添加如下属性
 ```java
    private final Map<Object, Integer> spans = new HashMap<>(); //存储组合span
    SpannableStringBuilder ss; //
    CharSequence text; //需要渲染的text
 ```

 - 内部类Spanedable，构造方法为default，不能外部创建其对象
```java
    Spanedable(CharSequence text) {
        this.text = text;
        this.ss = new SpannableStringBuilder(text);
    }
```

 - 大部分的span实现都是一致的，这里以StyleSpan和TextAppearanceSpan为例
```java
        /**
         * 字体样式
         *
         * @param style {@link android.graphics.Typeface}
         * @return
         */
        public Spanedable type(int style) { //传入字体样式，返回Spanedable的引用
            return type(style, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         * 字体样式
         *
         * @param style
         * @param flags
         * @return
         */
        public Spanedable type(int style, int flags) {//传入字体样式和flags，返回Spanedable的引用
            spans.put(new StyleSpan(style), flags);
            return this;
        }

        /**
         * @param family //字体
         * @param style //字体样式
         * @param size //文字大小
         * @param color //文字color
         * @param linkColor //链接color
         * @return
         */
        public Spanedable textAppearance(String family, int style, int size,
                                         ColorStateList color, ColorStateList linkColor) {
            return textAppearance(family, style, size, color, linkColor, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         * @param family
         * @param style
         * @param size
         * @param color
         * @param linkColor
         * @param flags
         * @return
         */
        public Spanedable textAppearance(String family, int style, int size, ColorStateList color, ColorStateList linkColor, int flags) {
            spans.put(new TextAppearanceSpan(family, style, size, color, linkColor), flags);
            return this;
        }
```

 - 新添加的background方法<br>
坐标图：<br>
![spantextview-background](/images/spantextview-background.png)<br>
代码实现如下(请参照坐标图分析代码实现的坐标计算)：
```java
         /**
         * 设置背景
         * @param drawable
         * @return
         */
        public Spanedable background(Drawable drawable) { //设置背景drawable
            return background(drawable, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         * 设置背景
         * @param drawable
         * @param flags
         * @return
         */
        public Spanedable background(Drawable drawable, int flags) {//设置背景drawable和flags
            return background(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), flags);
        }

        /**
         * 设置背景
         * @param drawable
         * @param flags
         * @return
         */
        public Spanedable background(Drawable drawable, final int w, final int h, int flags) { //通过重写ImageSpan的draw方法实现
            drawable.setBounds(0, 0, w, h); //设置drawable的bounds
            spans.put(new ImageSpan(drawable) {
                @Override
                public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
                                 int bottom, Paint paint) {
                    String sequence = text.subSequence(start, end).toString(); //渲染的text
                    Rect boundText = new Rect(); //测量text大小的rect
                    paint.getTextBounds(sequence, 0, sequence.length(), boundText); //填充boundText
                    Drawable b = getDrawable();
                    Rect bounds = b.getBounds();

                    int w = bounds.width() < boundText.width() ? boundText.width() : bounds.width(); //drawable最大宽度
                    int h = bounds.height();

                    /*
                    *设置drawable的最大高度
                    */
                    float fontHeight = boundText.height();
                    int maxHeight = (int) ((bottom - y) * 2 + fontHeight);
                    if (h < fontHeight) {
                        h = (int) fontHeight;
                    } else {
                        if (h > maxHeight) {
                            h = maxHeight;
                        }
                    }

                    b.setBounds(0, 0, w, h);

                    /*
                    paint.setColor(Color.WHITE);
                    canvas.drawRect(x + (bounds.width() - boundText.width()) / 2,
                            bottom - (bottom - y) - fontHeight,
                            (x + (bounds.width() - boundText.width()) / 2) + boundText.width(),
                            bottom - (bottom - y),
                            paint);
                    */
                    canvas.save();
                    int transY = top + (bottom - top - maxHeight) + (maxHeight - bounds.height()) / 2;
                    canvas.translate(x, transY); //平移画布
                    b.draw(canvas);
                    canvas.restore();
                    paint.setColor(Color.BLACK);
                    canvas.drawText(sequence, x + (bounds.width() - boundText.width()) / 2, y, paint); //绘制文字
                }
            }, flags);
            return this;
        }
```

 - 添加自定义的span
```java
        /**
         * @param obj
         * @return
         */
        public Spanedable span(Object obj) {
            return span(obj, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         * @param obj
         * @param flags
         * @return
         */
        public Spanedable span(Object obj, int flags) {
            spans.put(obj, flags);
            return this;
        }
```

 - 删除span
```java
        /**
         * @param obj
         */
        public void remove(Object obj) {
            ss.removeSpan(obj);
        }

        /**
         *
         */
        public void clear() {
            Iterator<Object> iterator = spans.keySet().iterator();
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
```

 - commit应用span
 ```java
        public TextView commit() {
            Iterator<Map.Entry<Object, Integer>> iterator = spans.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Integer> next = iterator.next();
                ss.setSpan(next.getKey(), 0, ss.length(), next.getValue());
            }
            SpanTextView.this.append(ss);
            return SpanTextView.this;
        }
```

## 如何使用
 - xml代码
```xml
<com.think.android.widget.SpanTextView
        android:id="@+id/spantextview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

 - java代码
```java
SpanTextView spanTextView = (SpanTextView) findViewById(R.id.spantextview);
        spanTextView.append("SpanTextView组合测试[");
        spanTextView.spanedable("ABCD").color(Color.RED).type(Typeface.ITALIC).absoluteSize(50, true).click(new SpanTextView.OnClickListener() {
            @Override
            public void onClick(CharSequence text) {
                Log.d(TAG, "onClick text = " + text);
            }
        }).commit();
        spanTextView.append("]组合测试");
```

## 后记
一切都是为了方便使用，也许这个控件封装的还有很多不足之处，欢迎大家指正，我将表示最真诚的感谢

本文[[示例代码]](https://github.com/borneywpf/ThinkAndroid)

---

## 参考资料
[Android中用Spannable在TextView中给文字加上边框](http://blog.csdn.net/candyguy242/article/details/13509591)

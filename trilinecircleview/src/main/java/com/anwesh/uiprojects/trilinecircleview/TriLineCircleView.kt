package com.anwesh.uiprojects.trilinecircleview

/**
 * Created by anweshmishra on 20/04/19.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.app.Activity
import android.content.Context

val nodes : Int = 5
val lines : Int = 3
val scGap : Float = 0.05f
val scDiv : Double = 0.51
val strokeFactor : Int = 90
val sizeFactor : Float = 2.9f
val foreColor : Int = Color.parseColor("#673AB7")
val backColor : Int = Color.parseColor("#BDBDBD")
val rotDeg : Float = 90f
val triSizeFactor : Float = 0.66f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()
fun Float.mirrorValue(a : Int, b : Int) : Float {
    val k : Float = scaleFactor()
    return (1 - k) * a.inverse() + k * b.inverse()
}
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * dir * scGap

fun Canvas.drawTriLineCircle(sc : Float, angleDeg : Float, size : Float, paint : Paint) {
    val deg : Double = angleDeg * 0.5f * Math.PI / 180
    val r : Float = size * triSizeFactor
    val x : Float = r * Math.sin(deg).toFloat()
    val y : Float = r * Math.cos(deg).toFloat()
    val rc : Float = size - r
    save()
    translate(0f, y)
    drawLine(-x, 0f, x, 0f, paint)
    drawArc(RectF(-x, -rc, x, rc), 0f, 180f * sc, false, paint)
    restore()
}

fun Canvas.drawTLCNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / sizeFactor
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    paint.color = foreColor
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    paint.style = Paint.Style.STROKE
    val angleBetweenLines : Float = 360f / lines
    save()
    translate(gap * (i + 1), h / 2)
    rotate(rotDeg * sc2)
    for (j in 0..(lines - 1)) {
        save()
        rotate(angleBetweenLines * j)
        drawTriLineCircle(sc1.divideScale(j, lines), angleBetweenLines, size, paint)
        restore()
    }
    restore()
}
/**
 * Copyright (C) 2023 multiPOS, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package cloud.multipos.pos.views

import cloud.multipos.pos.R
import cloud.multipos.pos.*
import cloud.multipos.pos.models.*
import cloud.multipos.pos.util.*
import cloud.multipos.pos.controls.*

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.view.View
import java.util.List
import android.view.Gravity
import android.widget.TextView
import android.graphics.Color

class TicketDisplay (val params: Jar): ListDisplay (Pos.app, null), PosDisplay, SwipeListener, ThemeListener {
		  		  
	 init {
		  		  
		  Themed.add (this)
		  Logger.d (params.stringify ())
	 }

	 /**
	  *
	  * ListDislay implementation
	  *
	  */
	 
	 override fun list (): MutableList <Jar> {

		  val items = mutableListOf <Jar> ()

		  for (item in Pos.app.ticket.items) {
				
				items.add (item)
		  }

		  for (tender in Pos.app.ticket.tenders) {

				items.add (tender)
		  }

		  return items
	 }
	 
	 override fun layout (): String {

		  return "ticket"
	 }

	 override fun listEntry (pos: Int, view: View): View {
		  				
					 var p = list.get (pos)
					 
					 when (p) {
						  
						  is TicketItem -> {

								return ItemLine (context, p, pos, this)
						  }
						  
						  is TicketItemAddon -> {
								
								return ItemAddonLine (context, p)
						  }
						  
						  is TicketTender -> {
								
								return TenderLine (context, p, Pos.app.ticket)
						  }
						  
						  else -> { }
					 }
					 
		  return view
	 }

	 /**
	  *
	  * Dislay impl
	  *
	  */
	 
	 override fun update () {
		  
		  // update the ticket items

		  if (Pos.app.ticket.hasItems ()) {
					 
				updateList ()
		  }
	 }
	 
	 override fun clear () {

		  clearSelect ()
		  updateList ()
	 }

	 override fun update (theme: Themes) {
		  
		  update ()  // force list redraw with new theme
	 }
	 
	 /**
	  *
	  * Swipe impl
	  *
	  */

	 override fun swipeUp () {

		  Logger.d ("ti... swipe up")

		  if (params.has ("swipe")) {
				
				swipeAction ("swipe_up");
		  }
	 }
	 
	 override fun swipeDown () {
		  
		  if (params.has ("swipe")) {
				
				swipeAction ("swipe_down");
		  }
	 }
	 
	 override fun swipeLeft () {
		  
		  if (params.has ("swipe")) {
				
				swipeAction ("swipe_left");
		  }
		  else {
				
				Control.factory ("LoadTicket").controlAction (Jar ()
																				  .put ("dir", 1))  // get previous suspended ticket
		  }
	 }
		  
	 override fun swipeRight () {
				
		  if (params.has ("swipe")) {
					 
				swipeAction ("swipe_right");
		  }
		  else {
				
				Control.factory ("LoadTicket").controlAction (Jar ()
																				  .put ("dir", -1))  // get next suspended ticket
		  }
	 }

	 fun swipeAction (swipeDir: String) {
		  
		  if (params.get ("swipe").has (swipeDir)) {
				
				val swipe = params.get ("swipe").get (swipeDir)
				Control.factory (swipe.getString ("control")).action (swipe.get ("params"))
		  }
	 }
}

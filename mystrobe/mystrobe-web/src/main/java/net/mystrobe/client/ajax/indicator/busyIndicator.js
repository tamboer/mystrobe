/**
 * Simple and effectiv busy / working indicator that also prevents unwanted user
 * interaction with the application until the Ajax request is finalized.
 * 
 * Requires JQuery.
 * 
 * HTML, add this line to the bottom of your HTML, just before the body tag
 * closes:
 * 
 * <div id="wrk_indicator"><div id="wrk_inner"> Working ...</div><div
 * id="wrk_overlay"> </div></div>
 * 
 * 
 * 
 */

WorkIndicator = {

	BlockUI : true

	/**
	 * The time out in milliseconds after wich the working sign is presented to
	 * the user
	 */
	,
	WorkingSignDisplayTimeout : 400

	/**
	 * The timeout in milliseconds after wich the overlay will be displayed over
	 * the screen preventing the user to click on anything until the AJAX request
	 * finishes
	 * 
	 * This value is important to be non zero in order to allow double click
	 * actions to happen. Wicket makes a request for the click event wich
	 * activates the overlay layer that prohibits the second click from the double
	 * click event
	 * 
	 * Changing this behavior due to MTS-107: it is possible to submit 2 times a
	 * form.
	 */
	,
	OverlayDisplayTimeout : 50

	/**
	 * Private variable holding the pid of the timeout process
	 */
	,
	hideTimeoutPid : null

	/**
	 * Private variable holding the pid of the timeout process
	 */
	,
	overlayTimeoutPid : null

	/**
	 * Simple function to allow the centering of the Woring sing indicator
	 */
	,
	Center : function(node) {
		node.css("position", "absolute");
		node.css("top", ($(window).height() - node.height()) / 2 + $(window).scrollTop() + "px");
		node.css("left", ($(window).width() - node.width()) / 2 + $(window).scrollLeft() + "px");
		return node;
	}

	/**
	 * Activate the overlay layer that prevents further interaction with the
	 * application until the current AJAX request finalizes
	 */
	,
	ActivateOverlay : function() {

		var showIndicator = true;

		var clickedElement = (window.event) ? event.srcElement : null;
		if (clickedElement != null) {
			var attribute = clickedElement.getAttribute("disableBigPopupAjaxIndicator");
			if (attribute != null && attribute.toUpperCase() == 'TRUE') {
				showIndicator = false;
			}
		}
		if (showIndicator) {
			$('#wrk_indicator').show();
		}
	}

	/**
	 * Clear the timeouts and hide the working sign and the overlay layer. If the
	 * time from activation is less than the time speciffied the layer will not
	 * have been displayed yet so it is enough to clear / reset the timeout
	 */
	,
	HideWorkingSign : function(jqEvent, attributes, jqXHR, errorThrown, textStatus) {

		if (WorkIndicator.overlayTimeoutPid != null) {
			clearTimeout(WorkIndicator.overlayTimeoutPid);
			clearTimeout(WorkIndicator.hideTimeoutPid);

			WorkIndicator.hideTimeoutPid = null;
			WorkIndicator.overlayTimeoutPid = null;
		}

		if (WorkIndicator.BlockUI){
			$('#wrk_overlay').hide();
			$('#wrk_inner').hide();
			$('#wrk_indicator').hide();
		}
	}

	/**
	 * Actually display the Working indicator
	 */
	,
	ShowWorkingSign : function() {
		WorkIndicator.Center($('#wrk_inner'));
		$('#wrk_overlay').show();
		$('#wrk_inner').show();
	}

	/**
	 * Activate the overlay layer and the working indicator display.
	 * 
	 * The overlay layer will be displayed after the WorkIndicator.ActivateOverlay
	 * period and not right away in order to allow double click actions to pass
	 * trough
	 * 
	 * After the WorkIndicator.OverlayDisplayTimeout ammount of time in
	 * milliseconds also the working sign will be displayed
	 */
	,
	ActivateWorkingSign : function(jqEvent, attributes, jqXHR, errorThrown, textStatus) {

		if (WorkIndicator.BlockUI) {
			if (WorkIndicator.overlayTimeoutPid != null) {
				// allow duplicate calls but make sure old time outs are cleared so we
				// do not block page
				clearTimeout(WorkIndicator.overlayTimeoutPid);
				clearTimeout(WorkIndicator.hideTimeoutPid);

				WorkIndicator.hideTimeoutPid = null;
				WorkIndicator.overlayTimeoutPid = null;
			}
                        
                        var showIndicator = true;

                        var clickedElement = document.getElementById(attributes.c);
                        if (clickedElement != null) {
                            var attribute = clickedElement.getAttribute("disableBigPopupAjaxIndicator");
                            if (attribute != null && attribute.toUpperCase() == 'TRUE') {
                                showIndicator = false;
                            }
                        }

                        if(showIndicator) {
                            WorkIndicator.overlayTimeoutPid = setTimeout(WorkIndicator.ActivateOverlay, WorkIndicator.OverlayDisplayTimeout);
                        }
                        
			WorkIndicator.hideTimeoutPid = setTimeout(WorkIndicator.ShowWorkingSign, WorkIndicator.WorkingSignDisplayTimeout);
		}
	}

	/**
	 * Set up the proper functions in the Wicket pre/post and failure handlers. To
	 * be invoked at loading.
	 * 
	 * Just add anywhere in the Javascript execution path
	 * $(document).ready(WorkIndicator.Initialize);
	 */
	,
	Initialize : function() {
		/* Load feedback */
		Wicket.Event.subscribe('/ajax/call/beforeSend', WorkIndicator.ActivateWorkingSign);
		/**Wicket.Event.subscribe('/ajax/call/failure', WorkIndicator.HideWorkingSign);**/
		Wicket.Event.subscribe('/ajax/call/complete', WorkIndicator.HideWorkingSign);
	}

};

$(document).ready(WorkIndicator.Initialize);

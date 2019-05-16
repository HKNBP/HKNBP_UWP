(function($){
	'use strict';

	/**
	 * Focusses the next :focusable element. Elements with tabindex=-1 are focusable, but not tabable.
	 * Does not take into account that the taborder might be different as the :tabbable elements order
	 * (which happens when using tabindexes which are greater than 0).
	 */
	$.focusNext = function(){
		selectNextTabbableOrFocusable(':focusable');
	};

	/**
	 * Focusses the previous :focusable element. Elements with tabindex=-1 are focusable, but not tabable.
	 * Does not take into account that the taborder might be different as the :tabbable elements order
	 * (which happens when using tabindexes which are greater than 0).
	 */
	$.focusPrev = function(){
		selectPrevTabbableOrFocusable(':focusable');
	};

	/**
	 * Focusses the next :tabable element.
	 * Does not take into account that the taborder might be different as the :tabbable elements order
	 * (which happens when using tabindexes which are greater than 0).
	 */
	$.tabNext = function(){
		selectNextTabbableOrFocusable(':tabbable');
	};

	/**
	 * Focusses the previous :tabbable element
	 * Does not take into account that the taborder might be different as the :tabbable elements order
	 * (which happens when using tabindexes which are greater than 0).
	 */
	$.tabPrev = function(){
		selectPrevTabbableOrFocusable(':tabbable');
	};

    function tabIndexToInt(tabIndex){
        var tabIndexInded = parseInt(tabIndex);
        if(isNaN(tabIndexInded)){
            return 0;
        }else{
            return tabIndexInded;
        }
    }

    function getTabIndexList(elements){
        var list = [];
        for(var i=0; i<elements.length; i++){
            list.push(tabIndexToInt(elements.eq(i).attr("tabIndex")));
        }
        return list;
    }

    function selectNextTabbableOrFocusable(selector){
        var selectables = $(selector);
        var current = $(':focus');

        // Find same TabIndex of remainder element
        var currentIndex = selectables.index(current);
        var currentTabIndex = tabIndexToInt(current.attr("tabIndex"));
        for(var i=currentIndex+1; i<selectables.length; i++){
            if(tabIndexToInt(selectables.eq(i).attr("tabIndex")) === currentTabIndex){
                selectables.eq(i).focus();
                return;
            }
        }

        // Check is last TabIndex
        var tabIndexList = getTabIndexList(selectables).sort(function(a, b){return a-b});
        if(currentTabIndex === tabIndexList[tabIndexList.length-1]){
            currentTabIndex = -1;// Starting from 0
        }

        // Find next TabIndex of all element
        var nextTabIndex = tabIndexList.find(function(element){return currentTabIndex<element;});
        for(var i=0; i<selectables.length; i++){
            if(tabIndexToInt(selectables.eq(i).attr("tabIndex")) === nextTabIndex){
                selectables.eq(i).focus();
                return;
            }
        }
    }

	function selectPrevTabbableOrFocusable(selector){
		var selectables = $(selector);
		var current = $(':focus');

		// Find same TabIndex of remainder element
        var currentIndex = selectables.index(current);
        var currentTabIndex = tabIndexToInt(current.attr("tabIndex"));
        for(var i=currentIndex-1; 0<=i; i--){
            if(tabIndexToInt(selectables.eq(i).attr("tabIndex")) === currentTabIndex){
                selectables.eq(i).focus();
                return;
            }
        }

        // Check is last TabIndex
        var tabIndexList = getTabIndexList(selectables).sort(function(a, b){return b-a});
        if(currentTabIndex <= tabIndexList[tabIndexList.length-1]){
            currentTabIndex = tabIndexList[0]+1;// Starting from max
        }

        // Find prev TabIndex of all element
        var prevTabIndex = tabIndexList.find(function(element){return element<currentTabIndex;});
        for(var i=selectables.length-1; 0<=i; i--){
            if(tabIndexToInt(selectables.eq(i).attr("tabIndex")) === prevTabIndex){
                selectables.eq(i).focus();
                return;
            }
        }
	}

	/**
	 * :focusable and :tabbable, both taken from jQuery UI Core
	 */
	$.extend($.expr[ ':' ], {
		data: $.expr.createPseudo ?
			$.expr.createPseudo(function(dataName){
				return function(elem){
					return !!$.data(elem, dataName);
				};
			}) :
			// support: jQuery <1.8
			function(elem, i, match){
				return !!$.data(elem, match[ 3 ]);
			},

		focusable: function(element){
			return focusable(element, !isNaN($.attr(element, 'tabindex')));
		},

		tabbable: function(element){
			var tabIndex = $.attr(element, 'tabindex'),
				isTabIndexNaN = isNaN(tabIndex);
			return ( isTabIndexNaN || tabIndex >= 0 ) && focusable(element, !isTabIndexNaN);
		}
	});

	/**
	 * focussable function, taken from jQuery UI Core
	 * @param element
	 * @returns {*}
	 */
	function focusable(element){
		var map, mapName, img,
			nodeName = element.nodeName.toLowerCase(),
			isTabIndexNotNaN = !isNaN($.attr(element, 'tabindex'));
		if('area' === nodeName){
			map = element.parentNode;
			mapName = map.name;
			if(!element.href || !mapName || map.nodeName.toLowerCase() !== 'map'){
				return false;
			}
			img = $('img[usemap=#' + mapName + ']')[0];
			return !!img && visible(img);
		}
		return ( /^(input|select|textarea|button|object)$/.test(nodeName) ?
			!element.disabled :
			'a' === nodeName ?
				element.href || isTabIndexNotNaN :
				isTabIndexNotNaN) &&
			// the element and all of its ancestors must be visible
			visible(element);

		function visible(element){
			return $.expr.filters.visible(element) && !$(element).parents().addBack().filter(function(){
				return $.css(this, 'visibility') === 'hidden';
			}).length;
		}
	}
})(jQuery);
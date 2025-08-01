$(document).ready(function() {
	function isMobileDevice() {
		const isMobileUA = /Mobi|Android|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
		const isSmallScreen = window.innerWidth <= 768; // you can adjust this breakpoint
		return isMobileUA || isSmallScreen;
	}

	if (isMobileDevice()) {

		// Add a mobile-specific class to body
		$('body').addClass('mobile-device');

		// Hide desktop-only elements
		$('.desktop-only').hide();

		// Show mobile-only elements
		$('.mobile-only').show();
		
		$(".billing-actions").css({
			"margin-top": "8%"
		});
		
		$(".grid-box").css({
			"margin-top": "3%"
		});
		$(".quantity-wrapper").css({
			"margin-left": "15%"
		});
		$("section").hide();

		$(".categories").show().css({
			"width": "100%",
		});

		/*$(document).click("sublist",function(){
			
			$(".product-box").show().css({
							"width": "300px",
						});
							
		});*/

		$(document).on("click", "#sublist, #list", function() {
			setTimeout(function() {
				var length = $(".quantity-wrapper div").length;
				if (length > 0) {
					$(".categories").hide();
					$(".product-box").show().css({
						"width": "100%"
					});
					$(".custom-btn").show()
					// Add buttons only if not already added
					if ($(".grid-content .custom-btn").length === 0) {
						$(".grid-content").append(`
			                    <button class="custom-btn btn1" id="catbtn">Back</button>
			                    <button class="custom-btn btn2" id="billbtn">Bill</button>
			                `);
					}
				}
			}, 500);
		});



		$(document).on("click", "#catbtn", function() {

		    // Check if .billing-form is visible
		    if ($(".billing-form").is(":visible")) {
		        $(".billing-form").hide()
		        $(".product-box").show();
				$(".custom-btn").show()
		        $(".categories").hide(); // Optionally hide categories
		    } else {
		       $(".btn1").hide()
		        $(".product-box").hide();
		        $(".categories").show().css({
		            "width": "100%"
		        });
		    }
		});


		$(document).on("click", "#billbtn", function() {
			
				const productRows = $('#TableDiv tr');
					if (productRows.length === 0) {
					    Swal.fire({
					        title: 'No Products Added!',
					        text: 'Please add at least one product before saving the bill.',
					        icon: 'warning',
					        timer: 2000,
					        showConfirmButton: false
					    });
					    return false;
					}
			
			else{
			
					$(".product-box").hide()
					$(".btn2").hide()
					$(".categories").hide();
					$('#TableDiv h3').remove(); 
					$(".billing-form").show().css({
						"width": "100%",
						"overflow": "auto"
					});
					var position = $('#totalspan').offset();
					$('#amount').css('top' , position.top);
					$('#amount').css('left' , position.left);
					$(".btn1").show()
					$(".billing-actions > button").each(function() {
						var buttonText = $(this).text().trim();
						var cleanText = buttonText.replace(/^[^a-zA-Z]+/, '');
		
						// Replace button content with cleaned text
						$(this).html(cleanText);
		
						// Apply CSS styling
						$(this).css({
							"width": "10%",
							"height": "90%"
						});
					});
		
				}
		});
		
	} else {
		console.log("Desktop device detected.");
	}
});




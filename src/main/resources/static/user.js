

$(document).ready(function() {
	
	 loadData();
	 $('#searchInput').on('input', function() {
	   let searchVal = $(this).val().toLowerCase().trim();
		
	   if(searchVal==""){
		loadData();
	   }
	   
	   $('#userTable tbody tr').each(function() {
	     let userId = $(this).find('td:eq(0)').text().toLowerCase();
	     let userName = $(this).find('td:eq(1)').text().toLowerCase();

	     if (userId.includes(searchVal) || userName.includes(searchVal)) {
	       $(this).show();
	     } else {
	       $(this).hide();
	     }
	   });
	 });

	$('#savebtn').on('click', function() {
		
		if (!validateUserForm() ) {
		       return false; 
		   }
		
		let userId = $("#userId").val();
		let userName = $("#userName").val();
		let userFullName = $("#userFullName").val();
		let userPassword = $("#userPassword").val();
		let userType = $("#userType").val();

		let payload = ({

			userId: userId,
			userName: userName,
			userFullName: userFullName,
			userPassword: userPassword,
			userType: userType
		});

		let url = 'userAjax.htm?method=saveUser';

		$.ajax({

			type: 'POST',
			url: url,
			data: ({ userData: JSON.stringify(payload) }),
			success: function(data) {

				userList = data.userList;
			},
			complete : function(){
				Swal.fire({
				        icon: 'success',
				        title: 'User saved successfully!',
				        showConfirmButton: false,
				        timer: 1500 // 1.5 seconds
				    });

				    setTimeout(function() {
				        window.location.reload();
				    }, 1500);
			}

		});

	});

	$('.editUsr').on('click', function() {
		
		let url = 'userAjax.htm?method=getIndividualUser&userId=' + $(this).data('code');

		$.ajax({

			type: 'POST',
			url: url,
			success: function(data) {

				user = data.user;
				$("#userId").val(user.userId).attr('disabled', true);
				$("#userName").val(user.userName);
				$("#userFullName").val(user.userFullName);
				$("#userPassword").val(user.userPassword);
				$("#userType").val(user.userType);
				$('#popupSheet').show();
			}

		});

	});

	$('#AddUsr').on('click', function() {

		$("#userId").val('').attr('disabled', false);
		$("#userName").val('');
		$("#userFullName").val('');
		$("#userPassword").val('');
		$("#userType").val('');
		$('#popupSheet').show();

	});
	
	$('.close-btn').on('click', function(){
		
		$('#popupSheet').hide();
		
	});

	$('.deleteUsr').on('click', function() {
		
		if (!confirm("Are you sure you want to delete this user?")) {
			return false;
		}
			
		let url = 'userAjax.htm?method=deleteUser&userId=' + $(this).data('code');
		
		$.ajax({

			type: 'POST',
			url: url,
			success: function(data) {
				if (data.saveData == 'Y') {
					Swal.fire({
						icon: 'success',
						title: 'Success!',
						text: 'User has been Deleted successfully.',
						timer: 1500,
						showConfirmButton: false
					});
				} else {
					Swal.fire({
						icon: 'warning',
						title: 'Warning!',
						text: 'Error while deleting User',
						confirmButtonColor: '#d33'
					});
				}
			},
			complete : function(){
						 window.location.reload();
			}
		});

	});
	
	$('#userId').on('blur', function() {

		let url = 'userAjax.htm?method=checkUserIdAlreadyExsists&userId=' + $(this).val();

		$.ajax({

			type: 'POST',
			url: url,
			success: function(data) {
					if(data.saveData == "Y")
					{
						 $('#userId').val('');
						 $('#userId').focus();
						 const userIdSpan = document.querySelector('.userIdSpan');
		                 userIdSpan.textContent = 'User ID already exists';
		                 userIdSpan.style.display = 'inline';
					}else{
						const userIdSpan = document.querySelector('.userIdSpan');
						userIdSpan.style.display = 'none';
						userIdSpan.textContent = 'Enter User Id';
					}
			}

		});		

	});
	
});


function loadData() {
	let url = 'userAjax.htm?method=getAllUser';

	$.ajax({

		type: 'POST',
		url: url,
		success: function(data) {

			userList = data.userList;
		}

	});

}


function validateUserForm() {
    let isValid = true;

    const userId = document.getElementById('userId').value.trim();
    const userName = document.getElementById('userName').value.trim();
    const userFullName = document.getElementById('userFullName').value.trim();
    const userPassword = document.getElementById('userPassword').value.trim();
    const userType = document.getElementById('userType').value;

    const userIdSpan = document.querySelector('.userIdSpan');
    if (userId === '') {
        userIdSpan.style.display = 'inline';
        isValid = false;
    } else {
        userIdSpan.style.display = 'none';
    }

    const userNameSpan = document.querySelector('.userNameSpan');
    if (userName === '') {
        userNameSpan.style.display = 'inline';
        isValid = false;
    } else {
        userNameSpan.style.display = 'none';
    }

    const userFullNameSpan = document.querySelector('.userFullNameSpan');
    if (userFullName === '') {
        userFullNameSpan.style.display = 'inline';
        isValid = false;
    } else {
        userFullNameSpan.style.display = 'none';
    }

    const userPasswordSpan = document.querySelector('.userPasswordSpan');
    if (userPassword === '') {
        userPasswordSpan.style.display = 'inline';
        isValid = false;
    } else {
        userPasswordSpan.style.display = 'none';
    }

    const userTypeSpan = document.querySelector('.userTypeSpan');
    if (userType === '') {
        userTypeSpan.style.display = 'inline';
        isValid = false;
    } else {
        userTypeSpan.style.display = 'none';
    }

    return isValid;
}

function closePopup() {
  document.getElementById("popupSheet").classList.remove("show");
}



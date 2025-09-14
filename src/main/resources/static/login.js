$(document).ready(function () {
  $(document).on('click', '#login', function (e) {
    e.preventDefault(); 

    let userId = $("#userId").val().trim();
    let userPassword = $("#userPassword").val().trim();

    $('#useridSpan').text('');
    $('#userPasswordSpan').text('');

    let isValid = true;

    if (!userId) {
      $('#useridSpan').text('Please Enter User Id');
      isValid = false;
    }

    if (!userPassword) {
      $('#userPasswordSpan').text('Please Enter Password');
      isValid = false;
    }

    if (!isValid) return false;


    let url = 'userAjax.htm?method=checkUser';

    $.ajax({
      type: 'POST',
      url: url,
      data: { 
		userId: userId,
		userPassword: userPassword
	 },
      success: function (data) {
		
        if (data.exsists == "Y") {
          $('#loginForm').submit();
        } else {
          alert('Invalid login. Please try again.');
        }
      },
      error: function () {
        alert('An error occurred while logging in.');
      }
    });

    return false;
  });
});

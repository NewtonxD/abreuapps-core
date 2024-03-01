/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
function dataPrepare(idForm){
    let d=$("#"+idForm+" :input").serializeArray();
    $("#"+idForm+" input[type=checkbox]").each(function() {
        if (!$(this)[0].checked) {
            d.push({name:$(this).attr("name"),value:"false"});
        }
    });
    return d;
}

$(function () {
    
    function passwordsMatch() {
        var newPassword = $('#newPassword').val();
        var confirmNewPassword = $('#confirmNewPassword').val();
        return newPassword === confirmNewPassword;
    }

    function checkPasswordStrength(password) {
        var strength = 0;

        if (password.length >= 8) {
            strength += 25;
        }

        // Check if password contains uppercase and lowercase letters
        if (password.match(/[a-z]/) && password.match(/[A-Z]/)) {
            strength += 25;
        }

        // Check if password contains numbers
        if (password.match(/\d/)) {
            strength += 25;
        }

        // Check if password contains special characters
        if (password.match(/[!@#;$%^&*(),.?":{}|<>]/)) {
            strength += 25;
        }

        // Update progress bar
        $('#passwordStrength').css('width', strength + '%').attr('aria-valuenow', strength);

        // Update progress bar color based on strength
        if (strength < 50) {
            $('#passwordStrength').removeClass('bg-warning bg-danger').addClass('bg-danger').text('Debil!');
        } else if (strength <= 75) {
            $('#passwordStrength').removeClass('bg-danger bg-success').addClass('bg-warning').text('Medianamente Segura');
        } else {
            $('#passwordStrength').removeClass('bg-warning bg-danger').addClass('bg-success').text('La más segura!');
        }

        // Display password message
        if (password.length === 0 || strength===100) {
            $('#passwordMessage').html('');
        } else {
            $('#passwordMessage').html('Una contraseña segura debe tener al menos 8 caracteres y contener como mínimo una letra mayúscula, una letra minúscula, un número y un carácter especial ( !@#$;%^&*(),.?":{}|<> ). ');
        }

        return strength;
    }

// Function to enable/disable submit button based on password match and non-empty new password
    function toggleSubmitButton() {
        checkPasswordStrength($('#newPassword').val());
        if (passwordsMatch() && checkPasswordStrength($('#newPassword').val()) === 100) {

            $('#btnGuardar').removeAttr('disabled');
        } else {
            $('#btnGuardar').attr('disabled', 'disabled');
        }
    }

    // Check password match on keyup
    $('#confirmNewPassword,#newPassword').keyup(function () {
        if (passwordsMatch() && $('#confirmNewPassword').val() !== '' && $('#newPassword').val() !== '') {
            $('#confirmNewPassword').css('border-color', 'green');
            $('#alert-diferent').css('display', 'none');
        } else {
            $('#alert-diferent').css('display', 'block');
            $('#confirmNewPassword').css('border-color', 'red');
        }
        toggleSubmitButton();
    });

    
    $("#form-guardar").on("submit",function(e){
        e.preventDefault();
        var data=dataPrepare("form-guardar");
        $.ajax({
            url:'/main/changeMyPwdNow',
            type:"POST",
            async:false,
            data:data,
            success: function(res){
                console.log(res);
                $("#msg").html("");
                $("#msg").prepend(
                `<div class="alert alert-${res.status}" role="alert">`+
                `   <span><b>${res.msg}</b></span>`+
                '</div>');
                if(res.status==="success"){
                    setTimeout(function() {
                        window.location.href = "/main/index";
                    }, 3000);
                }
            }
        });
    });

});
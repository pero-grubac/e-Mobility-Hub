// Client-side validation for every form with the "needs-validation" class:
// the browser's required/maxlength rules plus "end must be after start" for
// forms that have #startDate and #endDate.
(function () {
    'use strict';

    function checkDates(form) {
        var start = form.querySelector('#startDate');
        var end = form.querySelector('#endDate');
        if (!start || !end) {
            return;
        }
        var valid = !start.value || !end.value || end.value > start.value;
        end.setCustomValidity(valid ? '' : 'The end must be after the start.');
    }

    document.querySelectorAll('form.needs-validation').forEach(function (form) {
        form.addEventListener('input', function () {
            checkDates(form);
        });
        form.addEventListener('submit', function (event) {
            checkDates(form);
            // Deleting needs no valid fields.
            var submitter = event.submitter;
            if (submitter && submitter.value === 'delete') {
                return;
            }
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
})();

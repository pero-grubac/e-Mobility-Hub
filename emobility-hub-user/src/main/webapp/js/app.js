// Client-side checks and helpers for the eMobilityHub client app. The server
// runs the same checks again (Validators.java), these only give quick feedback.
(function () {
    'use strict';

    // Bootstrap validation styling on every form marked .needs-validation.
    document.querySelectorAll('form.needs-validation').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Password confirmation fields: data-match="#otherFieldId".
    document.querySelectorAll('[data-match]').forEach(function (input) {
        var other = document.querySelector(input.dataset.match);
        var check = function () {
            input.setCustomValidity(input.value === other.value ? '' : 'The passwords do not match.');
        };
        input.addEventListener('input', check);
        other.addEventListener('input', check);
    });

    // "Use my location" buttons: data-lat and data-lng point at the inputs to fill.
    document.querySelectorAll('[data-locate]').forEach(function (button) {
        button.addEventListener('click', function () {
            var lat = document.querySelector(button.dataset.lat);
            var lng = document.querySelector(button.dataset.lng);
            var status = document.querySelector(button.dataset.status);
            if (!navigator.geolocation) {
                status.textContent = 'Location is not available in this browser. Please type it in.';
                return;
            }
            status.textContent = 'Finding your location...';
            navigator.geolocation.getCurrentPosition(function (position) {
                lat.value = position.coords.latitude.toFixed(6);
                lng.value = position.coords.longitude.toFixed(6);
                status.textContent = 'Location found.';
            }, function () {
                status.textContent = 'Could not get your location. Please type it in.';
            }, { enableHighAccuracy: true, timeout: 10000 });
        });
    });

    window.emh = {
        luhn: function (value) {
            var digits = value.replace(/[\s-]/g, '');
            if (!/^[0-9]{13,19}$/.test(digits)) {
                return false;
            }
            var sum = 0;
            var doubleIt = false;
            for (var i = digits.length - 1; i >= 0; i--) {
                var d = parseInt(digits.charAt(i), 10);
                if (doubleIt) {
                    d *= 2;
                    if (d > 9) {
                        d -= 9;
                    }
                }
                sum += d;
                doubleIt = !doubleIt;
            }
            return sum % 10 === 0;
        },

        // MM/YY, valid until the end of that month.
        expiryValid: function (value) {
            var m = /^(0[1-9]|1[0-2])\/([0-9]{2})$/.exec(value.trim());
            if (!m) {
                return false;
            }
            var now = new Date();
            var year = 2000 + parseInt(m[2], 10);
            var month = parseInt(m[1], 10);
            return year > now.getFullYear() || (year === now.getFullYear() && month >= now.getMonth() + 1);
        },

        // Running clock and amount on the ride screen.
        startRideClock: function (startMillis, serverNowMillis, pricePerHour, clockEl, amountEl) {
            var offset = serverNowMillis - Date.now();
            var pad = function (n) { return (n < 10 ? '0' : '') + n; };
            var tick = function () {
                var seconds = Math.max(0, Math.floor((Date.now() + offset - startMillis) / 1000));
                clockEl.textContent = pad(Math.floor(seconds / 3600)) + ':' + pad(Math.floor(seconds / 60) % 60)
                    + ':' + pad(seconds % 60);
                var billable = Math.max(60, seconds) / 3600;
                amountEl.textContent = (Math.round(pricePerHour * billable * 100) / 100).toFixed(2) + ' EUR';
            };
            tick();
            setInterval(tick, 1000);
        }
    };
})();

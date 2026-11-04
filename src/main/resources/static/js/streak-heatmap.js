document.addEventListener('DOMContentLoaded', function () {
    var container = document.getElementById('streakHeatmapGrid');
    if (!container) return;

    fetch('/api/streaks/data')
        .then(response => response.json())
        .then(data => {
            renderHeatmap(data.activityDateMap || {});
        })
        .catch(err => console.error('Error fetching streak data:', err));

    function renderHeatmap(activityMap) {
        container.innerHTML = '';
        var today = new Date();

        // Generate dates for the past 16 weeks (112 days)
        var totalDays = 112;
        var startDate = new Date();
        startDate.setDate(today.getDate() - totalDays + 1);

        // Adjust startDate back to the previous Sunday to align rows
        var dayOfWeek = startDate.getDay(); // 0 is Sunday
        startDate.setDate(startDate.getDate() - dayOfWeek);

        var current = new Date(startDate);
        var cellsFragment = document.createDocumentFragment();

        while (current <= today) {
            var ymd = formatDate(current);
            var count = activityMap[ymd] || 0;
            var level = 0;
            if (count >= 4) level = 4;
            else if (count === 3) level = 3;
            else if (count === 2) level = 2;
            else if (count === 1) level = 1;

            var cell = document.createElement('div');
            cell.className = 'heatmap-cell heatmap-level-' + level;
            cell.setAttribute('data-bs-toggle', 'tooltip');
            cell.setAttribute('data-bs-placement', 'top');
            cell.setAttribute('title', count + ' activities on ' + ymd);

            cellsFragment.appendChild(cell);
            current.setDate(current.getDate() + 1);
        }

        container.appendChild(cellsFragment);

        // Re-initialize tooltips for newly added cells
        var tooltips = [].slice.call(container.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltips.map(function (el) {
            return new bootstrap.Tooltip(el);
        });
    }

    function formatDate(date) {
        var y = date.getFullYear();
        var m = String(date.getMonth() + 1).padStart(2, '0');
        var d = String(date.getDate()).padStart(2, '0');
        return y + '-' + m + '-' + d;
    }
});

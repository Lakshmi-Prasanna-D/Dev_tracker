document.addEventListener('DOMContentLoaded', function () {
    fetch('/api/analytics/data')
        .then(response => response.json())
        .then(data => {
            renderGoalChart(data);
            renderDsaTimelineChart(data);
            renderTopicChart(data);
            renderDifficultyChart(data);
            renderWeeklyChart(data);
            renderMonthlyChart(data);
            renderProjectChart(data);
            renderDailyActivityChart(data);
        })
        .catch(err => console.error('Error fetching analytics data:', err));

    function renderGoalChart(data) {
        var el = document.getElementById('goalCompletionChart');
        if (!el) return;
        var completed = data.completedGoals || 0;
        var pending = Math.max(0, (data.totalGoals || 0) - completed);

        new Chart(el, {
            type: 'doughnut',
            data: {
                labels: ['Completed Goals', 'Pending / In Progress'],
                datasets: [{
                    data: [completed, pending],
                    backgroundColor: ['#10b981', '#cbd5e1'],
                    borderWidth: 2,
                    borderColor: '#ffffff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '75%',
                plugins: {
                    legend: { position: 'bottom' }
                }
            }
        });
    }

    function renderDsaTimelineChart(data) {
        var el = document.getElementById('dsaTimelineChart');
        if (!el) return;
        var dsaMap = data.dsaProblemsSolvedOverTime || {};
        var labels = Object.keys(dsaMap);
        var values = Object.values(dsaMap);

        new Chart(el, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Problems Solved',
                    data: values,
                    borderColor: '#4f46e5',
                    backgroundColor: 'rgba(79, 70, 229, 0.1)',
                    tension: 0.35,
                    fill: true,
                    pointBackgroundColor: '#4f46e5',
                    pointRadius: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { precision: 0 },
                        grid: { color: '#f1f5f9' }
                    },
                    x: {
                        grid: { display: false }
                    }
                }
            }
        });
    }

    function renderTopicChart(data) {
        var el = document.getElementById('topicChart');
        if (!el) return;
        var topicMap = data.topicWiseDsaDistribution || {};
        var labels = Object.keys(topicMap);
        var values = Object.values(topicMap);

        new Chart(el, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Problems',
                    data: values,
                    backgroundColor: '#06b6d4',
                    borderRadius: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                indexAxis: 'y',
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    x: {
                        beginAtZero: true,
                        ticks: { precision: 0 },
                        grid: { color: '#f1f5f9' }
                    },
                    y: {
                        grid: { display: false }
                    }
                }
            }
        });
    }

    function renderDifficultyChart(data) {
        var el = document.getElementById('difficultyChart');
        if (!el) return;
        var diffMap = data.difficultyWiseDistribution || {};
        var labels = Object.keys(diffMap);
        var values = Object.values(diffMap);

        new Chart(el, {
            type: 'pie',
            data: {
                labels: labels,
                datasets: [{
                    data: values,
                    backgroundColor: ['#10b981', '#f59e0b', '#ef4444'],
                    borderWidth: 2,
                    borderColor: '#ffffff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'bottom' }
                }
            }
        });
    }

    function renderWeeklyChart(data) {
        var el = document.getElementById('analyticsWeeklyChart');
        if (!el) return;
        var weeklyMap = data.weeklyProductivity || {};
        var labels = Object.keys(weeklyMap);
        var values = Object.values(weeklyMap);

        new Chart(el, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Activities',
                    data: values,
                    backgroundColor: '#8b5cf6',
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { precision: 0 },
                        grid: { color: '#f1f5f9' }
                    },
                    x: {
                        grid: { display: false }
                    }
                }
            }
        });
    }

    function renderMonthlyChart(data) {
        var el = document.getElementById('analyticsMonthlyChart');
        if (!el) return;
        var monthlyMap = data.monthlyProductivity || {};
        var labels = Object.keys(monthlyMap);
        var values = Object.values(monthlyMap);

        new Chart(el, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Activities',
                    data: values,
                    backgroundColor: '#3b82f6',
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { precision: 0 },
                        grid: { color: '#f1f5f9' }
                    },
                    x: {
                        grid: { display: false }
                    }
                }
            }
        });
    }

    function renderProjectChart(data) {
        var el = document.getElementById('projectStatusChart');
        if (!el) return;
        var projMap = data.projectCompletionStats || {};
        var labels = Object.keys(projMap);
        var values = Object.values(projMap);

        new Chart(el, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: values,
                    backgroundColor: ['#64748b', '#4f46e5', '#10b981'],
                    borderWidth: 2,
                    borderColor: '#ffffff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '70%',
                plugins: {
                    legend: { position: 'bottom' }
                }
            }
        });
    }

    function renderDailyActivityChart(data) {
        var el = document.getElementById('dailyActivityChart');
        if (!el) return;
        var heatMap = data.dailyActivityHeatmap || {};
        var labels = Object.keys(heatMap);
        var values = Object.values(heatMap);

        new Chart(el, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Daily Activities',
                    data: values,
                    borderColor: '#10b981',
                    backgroundColor: 'rgba(16, 185, 129, 0.1)',
                    tension: 0.3,
                    fill: true,
                    pointBackgroundColor: '#10b981',
                    pointRadius: 3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { precision: 0 },
                        grid: { color: '#f1f5f9' }
                    },
                    x: {
                        grid: { display: false }
                    }
                }
            }
        });
    }
});

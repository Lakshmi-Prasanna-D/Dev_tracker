document.addEventListener('DOMContentLoaded', function () {
    var ctxWeekly = document.getElementById('weeklyProductivityChart');
    if (!ctxWeekly) return;

    fetch('/api/analytics/data')
        .then(response => response.json())
        .then(data => {
            var weeklyData = data.weeklyProductivity || {};
            var labels = Object.keys(weeklyData);
            var values = Object.values(weeklyData);

            new Chart(ctxWeekly, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Activities Completed',
                        data: values,
                        backgroundColor: '#4f46e5',
                        borderRadius: 6,
                        hoverBackgroundColor: '#4338ca'
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return context.parsed.y + ' activities';
                                }
                            }
                        }
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

            // DSA Status Doughnut on Dashboard if present
            var ctxDsa = document.getElementById('dsaRatioChart');
            if (ctxDsa) {
                new Chart(ctxDsa, {
                    type: 'doughnut',
                    data: {
                        labels: ['Solved', 'Attempted', 'Revisit'],
                        datasets: [{
                            data: [data.solvedDsaProblems || 0, (data.totalDsaProblems - data.solvedDsaProblems) || 0, 0],
                            backgroundColor: ['#10b981', '#f59e0b', '#06b6d4'],
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
        })
        .catch(err => console.error('Error loading dashboard chart data:', err));
});

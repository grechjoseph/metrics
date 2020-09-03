<h2>Running the application</h2>
<ol>
<li>Change <b>prerequisites/config/prometheus.yml</b> to have <b>scrape_configs.static_configs.targets</b> to point to the <b>IP</b> on which the application will be running.</li>
<li>Sign in to docker (since the Grafana image is not public).</li>
<li>Run command <b>cd /prerequisites</b> to switch to the directory where the docker-compose file is located.</li>
<li>Run command <b>docker-compose up -d</b> to run the docker-compose file silently.</li>
<li>Verify that Prometheus is up at: <a href="http://localhost:9090/graph">localhost:9090/graph</a>.</li>
<li>Verify that Grafana is up at: <a href="http://localhost:3000">localhost:3000</a> (default credentials <b>admin/admin</b>).</li>
<li>Run the application and call <a href="http://localhost:8080/info">localhost:8000/info</a> to verify it is up.</li>
</ol>

<h2>Connecting Prometheus to Application's metrics</h2>
<ol>
<li>Ensure docker-compose has been ran as described above.</li>
<li>Navigate to <a href="localhost:9090/graph">localhost:9090/graph</a>.</li>
<li>Once the page loads, provide "http_server_requests_seconds_count" in the query text field, and press 'Execute'.</li>
<li>In the console, a list of the retrieved metrics from the service should be obtained.</li>
<li>Switch to <a href="http://localhost:9090/graph?g0.range_input=1h&g0.expr=http_server_requests_seconds_count&g0.tab=0">Graph</a> instead of Console.</li>
<li>The Graph should be active with metrics showing.</li>
</ol>

<h2>Connecting Grafana to Prometheus</h2>
<ol>
<li>Navigate to <a href="http://localhost:3000">localhost:3000</a>.</li>
<li>Once Grafana loads, log in using default credentials <b>admin/admin</b>.</li>
<li>
Select Add Data Source and provide:
    <ul>
        <li><b>Name</b>: prometheus-local (or any other name)</li>
        <li><b>Type</b>: Prometheus</li>
        <li><b>URL</b>: http://[ip to prometheus, not localhost]:9090</li>
    </ul>
</li>
<li>Click <b>Save & Test</b> and verify that no errors are encountered.</li>
<li>From the Side Menu, select <b>Dashboard</b> -> <b>Home</b>, and click the <b>New Dashboard</b> button.</li>
<li>Select <b>Graph</b>.</li>
<li>Press the Graph's title and select <b>Edit</b>.</li>
<li>For data source, provide 'prometheus-local' (or Default if this was set to default).</li>
<li>
In the expression field, provide: <b>rate(http_server_requests_seconds_count{job="metrics-service"}[5m])</b>
    <ul>
    <li><b>rate(...)</b>: The function to use.</li>
    <li><b>http_server_requests_seconds_count</b>: The metrics dimension to use.</li>
    <li><b>job</b>: If we had multiple services, this would single out metrics for the service marked as job="metrics-service" in the properties.yml file done before.</li>
    <li><b>[5m]</b>: "per-second rate of HTTP requests as measured over the last 5 minutes"</li>
    <li><b>NOTE</b>: The query is in PromQL.</li>
    </ul>
</li>
<li>For the legend format, provide the following to make legends have a better aesthetic: <b>{{method}} - {{uri}} - {{status}}</b></li>
<li>Unfocus all text field and ensure the Graph is updated.</li>
<li>From the top right Menu, you may click the <b>Last 6 hours</b> button, and from the dropdown, set <b>Refreshing every</b> to <b>5s</b>, and press <b>Apply</b>. This will have th graph update every five seconds.</li>
<li>Once ready, click the <b>Save</b> icon from the top right menu, and name the Graph 'Requests Seconds Count'.</li>
<li>Once back out at the Dashboard, click <b>Save</b> from the top right menu, and name the Dashboard 'Requests Dashboard'.</li>
</ol>

<h2>Example Queries</h2>
<ul>
<li>Exclude requests made to /actuator/*<b>rate(http_server_requests_seconds_count{job = "metrics-service",uri !~ "/actuato.+"}[5m])</b></li>
<li>Percentage rates: <b>sum (http_server_requests_seconds_count{uri!~"/actuato.+", status="404"}) / sum (http_server_requests_seconds_count{uri!~"/actuato.+"}) * 100</b>.</li>
<li>Group by tag 'status': <b>sum by (status) (rate(items_total{methodName = "getItemById"}[30m]))</b></li>
</ul>

<h2>Custom Counter Metric</h2>
``` java
{%
final Counter longCounter = Counter.builder("info.submissions")
                .tag("length", "long")
                .description("Counter for long info.")
                .register(meterRegistry);

final Counter shortCounter = Counter.builder("info.submissions")
        .tag("length", "short")
        .description("Counter for short info.")
        .register(meterRegistry);

if(info.length() > 10) {
    longCounter.increment();
} else {
    shortCounter.increment();
}
%}
```
<h2>Custom Gauage Metric</h2>
``` java
Gauge.builder("info.length", () -> new AtomicInteger(this.info.length()).get())
                .strongReference(true)
                .register(meterRegistry);
```
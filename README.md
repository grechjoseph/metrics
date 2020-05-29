<h2>Running the application</h2>
<ol>
<li>Change prerequisites/config/prometheus.yml to have scrape_configs.static_configs.targets to point to the IP on which the application will be running.</li>
<li>Sign in to docker (since the Grafana image is not public).</li>
<li>Run command: cd /prerequisites.</li>
<li>Run command: docker-compose -d</li>
<li>Verify that Prometheus is up at: <a href="localhost:9090/graph">localhost:9090/graph</a>.</li>
<li>Verify that Grafana is up at: <a href="localhost:3000">localhost:3000</a> (default credentials admin/admin).</li>
<li>Run the application and call <a href="localhost:8080/info">localhost:8000/info</a> to verify it is up.</li>
</ol>

<2>Connecting Prometheus to Application's metrics</h2>
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
<li>Navigate to <a href="localhost:3000">localhost:3000</a>.</li>
<li>Once Grafana loads, log in using default credentials admin/admin.</li>
<li>
Select Add Data Source and provide:
    <ul>
        <li>Name: prometheus-local (or any other name)</li>
        <li>Type: Prometheus</li>
        <li>URL: http://[ip to prometheus, not localhost]:9090</li>
    </ul>
</li>
<li>Press Save & Test and verify that no errors are encountered.</li>
<li>From the Side Menu, select Dashboard -> Home, and click the New Dashboard button.</li>
<li>Select Graph.</li>
<li>Press the Graph's title and select Edit.</li>
<li>For data source, provide 'prometheus-local' (or Default if this was set to default).</li>
<li>
In the expression field, provide: rate(http_server_requests_seconds_count{job="metrics-service"}[5m])
    <ul>
    <li>rate(...): The Grafana function to use.</li>
    <li>http_server_requests_seconds_count: The metrics dimension to use.</li>
    <li>job: If we had multiple services, this would single out metrics for the service marked as job="metrics-service" in the properties.yml file done before.</li>
    <li>[5m]: "per-second rate of HTTP requests as measured over the last 5 minutes"</li>
    <li>NOTE: what's inside the curly brackets is PromQL.</li>
    </ul>
</li>
<li>For the legend format, provde the following to make legens have a better aesthetic: {{method}} - {{uri}} - {{status}}</li>
<li>Unfocus all text field and ensure the Graph is updated.</li>
<li>From the top right Menu, you may click the Last 6 hours button, and from the dropdown, set Refreshing ever to 5s, and press Apply. This will have th graph update every five seconds.</li>
<li>Once ready, click the Save icon from the top right menu, and name the Graph 'Requests Seconds Count'.</li>
<li>Once back out at the Dashboard, click Save from the top right menu, and name the Dashboard 'Requests Dashboard'.</li>
</ol>

<h2>Example Queries</h2>
<ul>
<li>rate(http_server_requests_seconds_count{job = "metrics-service",uri !~ "/actuato.+"}[5m]): Exclude requests made to /actuator/*</li>
</ul>
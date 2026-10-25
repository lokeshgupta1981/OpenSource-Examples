---
name: orderdesk-release
description: OrderDesk release steps, environment URLs, incident runbook and database migration rules. Use when releasing, deploying, handling an incident or planning a migration.
---

## Release process

1. Create a release branch named release/x.y from main.
2. Update the version in pom.xml.
3. Run the full test suite and the smoke tests.
4. Write release notes in docs/releases/x.y.md with every user-visible change.
5. Open a pull request to main and get two approvals.
6. Tag the merge commit as vx.y.0.
7. The CI pipeline builds the container image and pushes it to the registry.
8. Deploy to staging with the deploy job and wait for health checks.
9. Run the staging smoke test suite against https://staging.orderdesk.example.
10. Ask the product owner to sign off in the release ticket.
11. Deploy to production in the 10:00 to 12:00 window, never on Fridays.
12. Watch the dashboard for 30 minutes after deploying.
13. If the error rate goes above 1 percent, roll back with the rollback job.
14. Announce the release in the team channel with a link to the notes.

## Deployment environments

| Environment | URL | Database | Notes |
|---|---|---|---|
| local | http://localhost:8080 | in-memory | default profile |
| dev | https://dev.orderdesk.example | postgres-dev | deployed on every merge |
| staging | https://staging.orderdesk.example | postgres-staging | copy of production schema |
| production | https://orderdesk.example | postgres-prod | two regions |

## Incident runbook

### Orders return 500

Check the logs for the exception, check the database connection pool, and roll back the last deploy if it started after a release.

### Totals are wrong

Compare the coupon table with the expected rates, check TaxCalculator.TAX_RATE, and recompute a sample order by hand.

### Stock goes negative

Look for concurrent reservations of the same SKU and check InventoryService logs for the order IDs.

### Confirmations not sent

Check the mail relay status page and NotificationService logs; resend from the admin screen.

### Slow responses

Check the p95 latency panel, the database slow query log, and recent traffic spikes.

### Health check failing

Check the container restarts, memory usage, and the last configuration change.

## Database migration notes (planned move from in-memory to PostgreSQL)

1. Use Flyway for schema changes; one migration per pull request.
2. Name migrations V<number>__<description>.sql.
3. Never edit a migration that has run in any shared environment.
4. Money columns use NUMERIC(12,2).
5. Every table has created_at and updated_at columns.
6. Add indexes for every foreign key.
7. Order lines live in order_lines with a foreign key to orders.
8. Coupons move to a coupons table with code, rate, valid_from, valid_to.
9. Inventory moves to a stock table keyed by sku.
10. Run migrations against a copy of staging before production.

## Team contacts

- Orders and pricing: the checkout team channel
- Inventory: the warehouse systems team channel
- Release approvals: the product owner for checkout

# deadman

Provides a dead-man's-switch. Flow is:

1. PUT a timer description to a resource. This starts the timer.
2. Subsequent PUTs will restart the timer.
3. When the timer elapses, an SMS message will be sent according to the timer description.

Can DELETE timer descriptions. This will cancel the timer and not fire the SMS.

Can GET timer descriptions.

## Usage

Designed to deploy to Heroku. Configure Twilio using environment variables (see code).

### Example PUT

- timeout - length of timer in ms
- number - the phone number to send the SMS to
- message - the message to send in the SMS

```bash
curl -XPUT \
     -d '{"timeout" : 120000, "number" : "+15555558555", "message" : "Message"}' \
     -H "Content-Type: application/json" \
     https://myserver.herokuapp.com/timers/my-unique-reminder-id
```

## TODO

- Persist timers (currently they will disappear if the app restarts)
- Security (authentication)
- Error handling for missing params
- Alternate actions? I don't need this, so probably not.

## License

Copyright © 2014 Jimmy Thrasher

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

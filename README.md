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

### Example use

I use this project to remind me to take my FreedomPOP mifi home with me. Flow is as follows:

1. Cron job runs a script every minute: if on the FreedomPOP wifi hotspot, it will PUT a 2-minute timer. (The current_ssid is in this repo)
2. After 2 minutes of silence, I get an SMS reminding me to take my mifi home.

#### Cron config

```crontab
* * * * * /Users/jjthrash/bin/freedompop-deadman-switch
```

#### freedompop-deadman-switch

```bash
#!/bin/bash

ssid=`/Users/jjthrash/bin/current_ssid`

if [ "$ssid" = "FreedomPop" ]; then
    curl -XPUT \
         -d '{"timeout" : 120000, "number" : "+19195555555", "message" : "Don'"'"'t forget your FreedomPop!"}' \
         -H "Content-Type: application/json" \
         https://appname.herokuapp.com/timers/a262af3171c2bb838eadea92532d8476
fi
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

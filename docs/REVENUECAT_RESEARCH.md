# RevenueCat Integration Research

## Overview

RevenueCat is a subscription management platform that simplifies implementing and managing in-app purchases and subscriptions.

Instead of building a complete subscription system manually, RevenueCat handles the complexity of:

- Subscription management
- Purchase validation
- Customer management
- Entitlement management
- Subscription status tracking
- Revenue analytics
- App store integrations

RevenueCat allows developers to focus on building application features while RevenueCat manages subscription infrastructure.

For WithYou, RevenueCat will be considered for future premium features.

---

# Why WithYou May Need RevenueCat

The WithYou MVP focuses on building the core video-sharing experience.

MVP features include:

- Authentication
- User profiles
- Contacts
- Video upload
- Feed
- Video player
- Privacy controls
- Video analytics


Video analytics will be included in the MVP for testing and demonstration purposes.

However, future versions of WithYou may introduce premium features such as:

- Advanced video analytics
- AI-generated subtitles
- Cloud backup
- Additional storage
- Advanced privacy features


RevenueCat can be used in future versions to manage subscriptions and control access to premium features.

---

# RevenueCat Pricing

RevenueCat provides a free tier that can be used for development and testing.

During the WithYou development phase:

```
Cost = $0
```

The free plan allows developers to:

- Create RevenueCat projects
- Connect applications
- Integrate SDK
- Test subscription flows


Payment is only required when an application grows and exceeds the free usage limits.

---

# How RevenueCat Payment Works

RevenueCat does not process payments directly.

Payments are handled by the platform's app store.

For Android applications, the flow is:

```
User
 |
 |
WithYou Android App
 |
 |
RevenueCat SDK
 |
 |
Google Play Billing
 |
 |
Google Play Store
 |
 |
Payment Completed
 |
 |
RevenueCat Verifies Subscription
 |
 |
Premium Features Unlocked
```

Google Play handles:

- Payment processing
- Billing
- Refunds


RevenueCat handles:

- Subscription status
- Purchase validation
- User entitlements
- Subscription events

---

# RevenueCat Core Concepts

## 1. Customer

A customer represents an application user inside RevenueCat.

Each WithYou user will have a Firebase UID.

Example:

```
Firebase User ID:

user12345
```

This user can be connected with RevenueCat:

```
Firebase UID

      |

      |

RevenueCat Customer ID
```

This allows RevenueCat to associate subscriptions with the correct user.

---

# 2. Entitlements

Entitlements define access to premium features.

An entitlement represents a user's permission to access specific features.

Example:

```
Entitlement:

premium_access
```

Future WithYou premium features:

```
premium_access

        |

        |

----------------------

Advanced Analytics

AI Subtitles

Cloud Backup
```

If the entitlement is active, the user can access premium features.

---

# 3. Products

Products are subscription items created inside app stores.

Products are configured in:

- Google Play Console
- Apple App Store Connect


Example:

```
Product:

withyou_monthly

Price:

$2.99/month
```


Another product:

```
Product:

withyou_yearly

Price:

$24.99/year
```

---

# 4. Offerings

Offerings define which subscription products are displayed to users.

Example:

```
Default Offering

Packages:

- Monthly Subscription
- Yearly Subscription
```

The application retrieves offerings from RevenueCat and displays them in a paywall.

---

# 5. Customer Information

RevenueCat provides customer information including:

- Active subscriptions
- Expiration dates
- Purchase history
- Active entitlements


The application checks this information before unlocking premium features.

---

# Future WithYou RevenueCat Architecture

Future subscription architecture:

```
Firebase Authentication

        |

        |

Firebase User UID

        |

        |

RevenueCat Customer

        |

        |

Premium Entitlement

        |

        |

Premium Features
```

---

# Future Android Integration Flow

## Step 1 — Create RevenueCat Project

A RevenueCat account is created.

The Android application is connected using:

```
Application Package Name:

com.withyou.app
```

RevenueCat provides an SDK API key.

---

## Step 2 — Install RevenueCat SDK

The RevenueCat SDK is added to the Android project.

Example:

```
implementation(
"com.revenuecat.purchases:purchases"
)
```

---

## Step 3 — Configure RevenueCat SDK

RevenueCat is initialized when the application starts.

Example:

```
Purchases.configure(
    RevenueCat API Key
)
```

After configuration, the application can communicate with RevenueCat.

---

## Step 4 — Connect Firebase Users

When a user logs into WithYou:

```
User Login

      |

      |

Firebase Authentication

      |

      |

Firebase UID

      |

      |

RevenueCat Customer ID
```

The subscription information becomes connected to the user's account.

---

## Step 5 — Check Premium Status

When a user accesses a premium feature:

```
User Opens Premium Feature

          |

          |

Check RevenueCat Entitlement

          |

      -----------------

      |               |

   Active          Inactive

      |               |

Allow Access     Show Paywall
```

---

# Firestore Database Impact

RevenueCat will not be included in the current MVP database schema.

The MVP database will only store application data:

- Users
- Profiles
- Contacts
- Videos
- Privacy settings
- Analytics data


When subscriptions are introduced in the future, the user document can be extended:

```
users

 |
 |
 userId

      |
      |
      ├── username
      ├── email
      ├── bio
      └── revenueCatId
```

RevenueCat will remain the source of truth for subscription status.

Firestore should not store sensitive payment information.

---

# RevenueCat Events

RevenueCat can send events whenever subscription changes occur.

Examples:

- New subscription
- Subscription renewal
- Subscription cancellation
- Subscription expiration
- Refund


Event flow:

```
Google Play Store

        |

        |

RevenueCat Servers

        |

        |

Subscription Event

        |

        |

Connected Services
```

RevenueCat detects subscription changes directly from app stores, even when users are not actively using the application.

---

# Future Premium Feature Plan for WithYou

| Feature | MVP | Future Premium |
|---|---|---|
| Upload Videos | Yes | Yes |
| Contacts | Yes | Yes |
| Video Sharing | Yes | Yes |
| Basic Analytics | Yes | Yes |
| Advanced Analytics | No | Yes |
| AI Subtitles | No | Yes |
| Cloud Backup | No | Yes |
| Additional Storage | No | Yes |

---

# MVP Decision

RevenueCat will not be implemented in the MVP.

The MVP will include analytics as a core application feature for testing and demonstration.

RevenueCat will be introduced in future versions when premium subscriptions are required.

Future RevenueCat integration will manage:

- Subscription purchases
- Premium entitlement verification
- Access control
- Subscription lifecycle management


Payment provider:

```
Google Play Billing
```

---

# Final Decision

RevenueCat is selected as the future subscription management solution for WithYou because:

- It reduces billing complexity
- It provides subscription management
- It handles purchase verification
- It supports entitlement-based access control
- It integrates with Google Play Billing
- It has a free development plan
- It supports future scaling


WithYou will integrate RevenueCat after the MVP when premium features and monetization are introduced.
# Contextual Cards

## About

Contextual Cards are cards / views whose UI properties, along with data can be manipulated by the API. They are useful for making customised UI for every user. Major apps like Facebook, Instagram, Youtube, Play Store, etc use these. This project was assigned to me by the Fampay Talent Team.

## This project is made according to the following requirements

- Develop a standalone container, that displays a list of `Contextual Cards`
- This container should work completely independently of everything else, such that, we can add this to container to any fragment/activity and it should work. (Plug-and-Play component)
- Your app should render contextual cards in a list based on the API response that you get from this [API](https://run.mocky.io/v3/fefcfbeb-5c12-4722-94ad-b8f92caad1ad)
- The design specifications for the different design types can be referenced from design specified [here.](https://www.figma.com/file/AvK2BRGwMTv4kQab5ymJ0K/AAL3-Android-assignment-Design-Specs) (To access and download the assets please login into Figma)
- The design linked is only for reference for different types of design types of cards. Actual response from API will render different results.

## Installation

Clone this repository and import it into **Android Studio**
```bash
git clone https://github.com/werainkhatri/contextualcards
```

## Generating signed APK

From Android Studio:
1. ***Build*** menu
2. ***Generate Signed APK...***
3. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*

## Technical Details
- Framework - **Native Android** (Kotlin + XML)
- Language - **Kotlin** (v 1.4.30)
- Architectural Pattern - **Model View ViewModel** (MVVM)
- Libraries Used - Retrofit, Kotlin Coroutines, Glide, Facebook's Shimmer, ViewModel and LiveData

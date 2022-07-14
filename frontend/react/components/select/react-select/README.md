# React select

`Version - v4.3.1`

## Pros
:white_check_mark: Excellent [documentation]('https://react-select.com/')

:white_check_mark: Fully customizable

:white_check_mark: Easy to use with [portals]('https://reactjs.org/docs/portals.html')

:white_check_mark: Has typescript types that are easy to import

:white_check_mark: It’s very easy to find answers on StackOverflow if you are faced with some problems

:white_check_mark: You can add any children components, not as options of select but to be used as some dropdown elements

:white_check_mark: Has [autocomplete](frontend/react/components/select/react-select/AutocompleteSelect.tsx) option with async requests



## Cons
:x: A lot of code for Select as UI component

:x: Styles must be with CSS in JS

:x: Extra hard to make it fluid width based on word width

:x: Not so obvious how to integrate it with form but we have the [approach]('./SelectAdapter.js')


## Pay Attention
:exclamation: If you use Next.js and portals this component should be dynamically loaded

:exclamation: If Lighthouse performance is important for you, you must find a way to decrease a lot of DOM elements that this library renders in the HTML document

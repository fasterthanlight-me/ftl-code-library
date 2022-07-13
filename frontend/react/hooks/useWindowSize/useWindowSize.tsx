import {
  useState,
  useEffect,
  createContext,
  FunctionComponent,
  ReactNode,
  useContext,
} from 'react'
import breakpoints from './breakpoints.module.scss'

interface DeviceDetectorResults {
  isMobile: boolean
}

const { xs, sm, md, lg, xl, xxl } = breakpoints
const breakpointsOrder = [xs, sm, md, lg, xl, xxl]

const getBreakpointMax = (breakpoint) => {
  const nextBreakpointIndex = breakpointsOrder.indexOf(breakpoint) + 1

  return `${parseInt(breakpointsOrder[nextBreakpointIndex], 10) - 0.02}px`
}

interface WindowSize {
  width?: number
  height?: number
  isXs: boolean
  isSm: boolean
  isMd: boolean
  isLg: boolean
  isXl: boolean
  isXxl: boolean
  isMobile: boolean
  isDesktop: boolean
}

// @ts-ignore
const WindowSizeContext = createContext<WindowSize>(null)

const getBreakpointMinMaxMediaQuery = (minBreakpointWidth) =>
  `(min-width: ${minBreakpointWidth}) and (max-width: ${getBreakpointMax(
    minBreakpointWidth,
  )})`

// Hook
const useWindowSize = (): WindowSize => {
  const context = useContext(WindowSizeContext)

  if (context === undefined) {
    throw new Error('useWindowSize must be used within Provider')
  }

  return context
}

export const WindowSizeProvider: FunctionComponent<{
  children: ReactNode
  deviceDetectorResults?: DeviceDetectorResults
}> = ({ children, deviceDetectorResults }) => {
  const [windowSize, setWindowSize] = useState<WindowSize>(() => ({
    isXs: false,
    isSm: false,
    isMd: false,
    isLg: false,
    isXl: false,
    isXxl: false,
    isMobile: !!deviceDetectorResults?.isMobile,
    isDesktop: false,
  }))

  useEffect(() => {
    // Handler to call on window resize
    function handleResize() {
      // Set window width/height to state
      const values = {
        width: innerWidth,
        height: innerHeight,
        isXs: window.matchMedia(`(max-width: ${getBreakpointMax(xs)})`).matches,
        isSm: window.matchMedia(getBreakpointMinMaxMediaQuery(sm)).matches,
        isMobile: window.matchMedia(`(max-width: ${getBreakpointMax(sm)})`)
          .matches,
        isMd: window.matchMedia(getBreakpointMinMaxMediaQuery(md)).matches,
        isLg: window.matchMedia(getBreakpointMinMaxMediaQuery(lg)).matches,
        isXl: window.matchMedia(getBreakpointMinMaxMediaQuery(xl)).matches,
        isXxl: window.matchMedia(`(min-width: ${xxl})`).matches,
        isDesktop: window.matchMedia(`(min-width: ${xl})`).matches,
      }

      setWindowSize(values)
    }

    // Add event listener
    window.addEventListener('resize', handleResize)

    // Call handler right away so state gets updated with initial window size
    handleResize()

    // Remove event listener on cleanup
    return () => window.removeEventListener('resize', handleResize)
  }, []) // Empty array ensures that effect is only run on mount

  return (
    <WindowSizeContext.Provider value={windowSize}>
      {children}
    </WindowSizeContext.Provider>
  )
}

export default useWindowSize

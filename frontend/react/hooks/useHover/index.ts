import React, { useState } from 'react'

export interface UseHoverOptions {
  mouseEnterDelayMS?: number
  mouseLeaveDelayMS?: number
}

export type HoverProps = Pick<
  React.HTMLAttributes<HTMLElement>,
  'onMouseEnter' | 'onMouseLeave'
>

type timeOutType = ReturnType<typeof setTimeout>

export default function useHover({
  mouseEnterDelayMS = 0,
  mouseLeaveDelayMS = 0,
}: UseHoverOptions = {}): [boolean, HoverProps] {
  const [isHovering, setIsHovering] = useState(false)
  let mouseEnterTimer: timeOutType | undefined
  let mouseOutTimer: timeOutType | undefined

  return [
    isHovering,
    {
      onMouseEnter: (): void => {
        clearTimeout(mouseOutTimer)
        mouseEnterTimer = setTimeout(
          () => setIsHovering(true),
          mouseEnterDelayMS,
        )
      },
      onMouseLeave: (): void => {
        clearTimeout(mouseEnterTimer)
        mouseOutTimer = setTimeout(
          () => setIsHovering(false),
          mouseLeaveDelayMS,
        )
      },
    },
  ]
}

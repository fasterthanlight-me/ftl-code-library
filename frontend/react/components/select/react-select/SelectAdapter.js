import React from 'react'
import dynamic from 'next/dynamic'
const Select = dynamic(() => import('components/ui/select'), { ssr: false })

const SelectAdapter = ({ input, className = '', ...rest }) => (
  <Select
    {...input}
    {...rest}
    className={className}
    onChange={(value) => {
      input.onChange(value)
      rest.onChange?.(value)
    }}
    searchable
    withPortal
  />
)

export default SelectAdapter

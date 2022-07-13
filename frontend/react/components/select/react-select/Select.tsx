import React, { ReactElement, useCallback, useRef, useState } from 'react'
import ReactSelect, {
  components,
  Props as ReactSelectProps,
  OptionTypeBase,
} from 'react-select'
import cs from 'classnames'
import SVG from 'react-inlinesvg'
import Checkbox from 'components/ui/checkbox'
import CounterBadge from 'components/ui/counter-badge'
import ChevronDown from 'assets/icons/chevron-down-bold.svg'
import customStyles from './styles'
import colors from 'assets/styles/modules/colors.module.scss'
import styles from './Select.module.scss'
import {
  convertGTMDataToAttributes,
  getBaseGTMData,
} from '../../../lib/helpers/events'
import { DROPDOWN_FILTER } from '@constants/events/promo'

/* eslint-disable react/destructuring-assignment */
// eslint-disable-next-line react/display-name
const getMenuListWithChildren = (children, ref) => (props) => {
  return (
    <components.MenuList
      {...props}
      className={styles.menuList}
      innerRef={(el) => {
        props.innerRef(el)
        ref.current = el
      }}
    >
      {props.children}
      {children}
    </components.MenuList>
  )
}

const getDropdown = (customIndicator) => (props) => {
  return (
    <components.DropdownIndicator {...props}>
      <SVG src={customIndicator || ChevronDown} />
    </components.DropdownIndicator>
  )
}

const CheckboxOption = (props) => (
  <components.Option {...props} getStyles={() => null}>
    <Checkbox
      id={props.value}
      isChecked={props.isSelected}
      onChange={() => null}
      isDisabled
    >
      {props.label}
    </Checkbox>
  </components.Option>
)

const getMenuPlaceholder = (withCounterBadge) => (props) => {
  const selectedOptionsCount = props.getValue().length
  return (
    <components.Placeholder {...props}>
      {props.children}{' '}
      {withCounterBadge && selectedOptionsCount > 0 && (
        <CounterBadge
          className={styles.counter}
          number={selectedOptionsCount}
        />
      )}
    </components.Placeholder>
  )
}

const SimpleOption = (props) => {
  const optionGtmTags = getBaseGTMData(
    DROPDOWN_FILTER,
    `${DROPDOWN_FILTER} -> ${props.selectProps.placement}`,
    `${DROPDOWN_FILTER} -> ${props.selectProps.placement} -> ${props.selectProps.placeholder} -> ${props.label}`,
  )
  const [hover, setHover] = useState(false)
  const hoverStyles = hover
    ? {
        backgroundColor: '#F8F7FC',
        color: colors.dark,
      }
    : null
  const optionStyles = {
    ...props.getStyles('option', props),
    ...hoverStyles,
  }
  return (
    <div
      onClick={props.innerProps.onClick}
      {...props.innerProps}
      id={props.innerProps.id}
      onMouseEnter={() => {
        setHover(true)
      }}
      onMouseLeave={() => {
        setHover(false)
      }}
      style={optionStyles}
      tabIndex={props.innerProps.tabIndex}
      {...convertGTMDataToAttributes(optionGtmTags)}
    >
      {props.children}
    </div>
  )
}
const Menu = (props) => {
  return <components.Menu className={styles.menu} {...props} />
}

const Control = (props) => {
  return (
    <components.Control
      className={cs(styles.control, { [styles.active]: props.hasValue })}
      {...props}
    >
      {props.children}
    </components.Control>
  )
}

export interface Option extends OptionTypeBase {
  [key: string]: unknown
}

// @ts-expect-error
export interface Props extends ReactSelectProps {
  placeholder?: string
  isClearable?: boolean
  isMulti?: boolean
  name?: string
  showLabels?: boolean
  withCheckboxes?: boolean
  classNamePrefix?: string
  withPortal?: boolean
  children?: ReactElement
  error?: any
  touched?: boolean
  outerStyles?: any
  withDropdownIndicator?: boolean
  withCounterBadge?: boolean
  customDropdownIndicator?: ReactElement
  placement?: string
}

const NoNoOptionsMessage = () => {
  return null
}
//Fix for LightHouse issue "Form elements do not have associated labels "
const SeoInput = (props) => {
  return (
    <components.Input
      aria-label={props.name}
      aria-labelledby={props.name}
      {...props}
    />
  )
}

const Select: React.FC<Props> = ({
  placeholder = 'Choose an option',
  isClearable = false,
  showLabels,
  withCheckboxes,
  classNamePrefix,
  withPortal,
  children = null,
  isMulti,
  className,
  isLoading = false,
  error,
  touched,
  outerStyles,
  components,
  onChange,
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  withDropdownIndicator = true,
  customDropdownIndicator = null,
  withCounterBadge = true,
  value,
  style,
  ...props
}) => {
  const [localMenuIsOpen, setMenuIsOpen] = useState(false)
  const childrenRef = useRef(null)
  const processBlur = useCallback(
    (event) => {
      if (
        children &&
        // @ts-ignore
        childrenRef.current?.contains?.(event.nativeEvent.relatedTarget)
      ) {
        setMenuIsOpen(true)
        // @ts-ignore
        event.nativeEvent.relatedTarget.addEventListener(
          'blur',
          () => event.target.focus(),
          { once: true },
        )
      } else {
        setMenuIsOpen(false)
      }
    },
    [childrenRef, children],
  )
  return (
    <>
      <ReactSelect
        {...props}
        className={className}
        classNamePrefix={classNamePrefix}
        closeMenuOnSelect={!isMulti}
        components={{
          ...components,
          ...{ DropdownIndicator: getDropdown(customDropdownIndicator) },
          Option: withCheckboxes ? CheckboxOption : SimpleOption,
          ...(children && {
            MenuList: getMenuListWithChildren(children, childrenRef),
          }),
          ...(children && {
            Placeholder: getMenuPlaceholder(withCounterBadge),
          }),
          Menu,
          Control,
          ...(children && { NoOptionsMessage: NoNoOptionsMessage }),
          Input: SeoInput,
        }}
        controlShouldRenderValue={showLabels}
        hideSelectedOptions={false}
        inputProps={{ 'aria-label': props.name, 'aria-labelledby': props.name }}
        isClearable={isClearable}
        isLoading={isLoading}
        isMulti={isMulti}
        menuIsOpen={children ? localMenuIsOpen : undefined}
        menuPortalTarget={withPortal ? document.body : undefined}
        onBlur={processBlur}
        onChange={onChange}
        onFocus={() => {
          if (children) {
            setMenuIsOpen(true)
          }
        }}
        placeholder={placeholder}
        styles={{ ...outerStyles, ...style } || { ...customStyles, ...style }}
        value={value}
      />
      {touched && error && <div className={styles.error}>{error}</div>}
    </>
  )
}

export default Select

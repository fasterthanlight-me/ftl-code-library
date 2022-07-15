### Example

```
import useHover from 'hooks/useHover'

const HomePage: React.FC = () => {
  const [isHovering, hoverProps] = useHover();

  return (
    <>
      <span {...hoverProps} aria-describedby='overlay'>
        Hover me
      </span>
      {isHovering && <div>Show / Hide</div>}
    </>
  )
}
export default HomePage

```

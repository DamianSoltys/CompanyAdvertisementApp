import { trigger, state, style, animate, transition, query, animateChild, group } from '@angular/animations';

export const slideInAnimation = trigger('routeAnimations', [
  transition('* <=> *', [
    query(
      ':enter, :leave',
      [
        style({
          position: 'absolute',
          width: '100%',
          opacity: 0,
          transform: 'scale(0) translateY(100%)'
        }),
        animateChild()
      ],
      { optional: true }
    ),
    query(
      ':enter',
      [
        animate(
          '600ms ease',
          style({
            opacity: 1,
            transform: 'scale(1) translateY(0)'
          })
        ),
        animateChild()
      ],
      { optional: true }
    )
  ])
]);

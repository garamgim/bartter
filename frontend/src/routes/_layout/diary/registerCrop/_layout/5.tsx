import { createFileRoute, useNavigate } from '@tanstack/react-router';
import classnames from 'classnames/bind';
import Lottie from 'react-lottie-player';

import RegisterAnimation from '@/assets/lottie/register.json';
import GeneralButton from '@/components/Buttons/GeneralButton.tsx';
import Heading from '@/components/Heading';
import useRootStore from '@/store';

import styles from '../registerCrop.module.scss';

const cx = classnames.bind(styles);

export const Route = createFileRoute('/_layout/diary/registerCrop/_layout/5')({
  component: CropProfilePage,
});

function CropProfilePage() {
  const { nickname, date, description, image, initialImage, addCrop, setActiveComponent, resetCropForm } = useRootStore(state => state);

  const navigate = useNavigate();

  const handleRegisterComplete = () => {
    addCrop({
      id: Date.now(), // 임의의 ID 할당, 필요시 다른 방식으로 변경
      nickname,
      image: image || initialImage,
      date,
      description,
      name: nickname
    });
    setActiveComponent('내 작물');
    navigate({
      to: '/diary',
      replace: true,
    }).then(() => {
      resetCropForm(); // 데이터 리셋
    });
  };

  const displayImage = image || initialImage;

  return (
    <>
      <div className={cx('headingContainer')}>
        <Heading>나만의 작물이 등록되었습니다.</Heading>
      </div>
      <Lottie loop animationData={RegisterAnimation} play className={cx('animation')} />
      <div className={cx('noteStyle')}>
        <div className={cx('leftSection')}>
          {displayImage && <img src={displayImage} alt={`${nickname}의 이미지`} className={cx('cropImage')} />}
          <div className={cx('nickname')}>{nickname}</div>
        </div>
        <div className={cx('rightSection')}>
          <div className={cx('date')}>처음 만난 날짜: {date}</div>
          <div className={cx('description')}>{description}</div>
        </div>
      </div>
      <div className={cx('buttonContainer')}>
        <GeneralButton
          buttonStyle={{ style: 'primary', size: 'large' }}
          onClick={handleRegisterComplete}
        >
          완료
        </GeneralButton>
      </div>
    </>
  );
}

export default CropProfilePage;